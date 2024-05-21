package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.exception.*;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestDto;
import ru.practicum.ewm.request.model.RequestMapper;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<RequestDto> getOwnRequests(long userId) {
        if (userRepository.existsById(userId)) {
            return requestRepository.findByUserId(userId).stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
        } else throw new UserNotFoundException(userId);
    }

    @Override
    public RequestDto createRequest(long userId, long eventId) {
        if (userRepository.existsById(userId)) {
            if (eventRepository.existsById(eventId)) {
                Event event = eventRepository.findById(eventId).get();
                if (event.getInitiator().getId() == userId)
                    throw new RequestFromInitiatorException("You are initiator!");
                if (event.getState() != EventState.PUBLISHED)
                    throw new EventNotPublishedException("Event not published.");
                if (event.getConfirmedRequests() >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
                    throw new EventLimitExceededException("Limit of requests is reached.");
                }
                Request request = Request.builder()
                        .status(RequestStatus.PENDING)
                        .created(LocalDateTime.now())
                        .requester(userRepository.findById(userId).get())
                        .event(event)
                        .build();
                if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    eventRepository.save(event);
                }
                return RequestMapper.toRequestDto(requestRepository.save(request));
            } else throw new EventNotFoundException(eventId);
        } else throw new UserNotFoundException(userId);
    }

    @Override
    public RequestDto cancelRequest(long userId, long requestId) {
        if (userRepository.existsById(userId)) {
            if (eventRepository.existsById(requestId)) {
                Request request = requestRepository.findById(requestId).get();
                if (request.getRequester().getId() == userId) {
                    if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
                        Event updatedEvent = request.getEvent();
                        updatedEvent.setConfirmedRequests(updatedEvent.getConfirmedRequests() - 1);
                        eventRepository.save(updatedEvent);
                    }
                    request.setStatus(RequestStatus.CANCELED);
                    return RequestMapper.toRequestDto(request);
                } else throw new RequestNotFoundException(requestId);
            } else throw new RequestNotFoundException(requestId);
        } else throw new UserNotFoundException(userId);
    }
}
