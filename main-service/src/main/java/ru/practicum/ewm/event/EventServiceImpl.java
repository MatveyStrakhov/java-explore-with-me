package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.*;
import ru.practicum.ewm.event.model.AnswerRequestResultDto;
import ru.practicum.ewm.event.model.location.Location;
import ru.practicum.ewm.event.model.location.LocationMapper;
import ru.practicum.ewm.exception.*;
import ru.practicum.ewm.formatter.CustomDateTimeFormatter;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestDto;
import ru.practicum.ewm.request.model.RequestMapper;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.stats.client.StatsClient;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;

    @Override
    public EventFullDto createEvent(long userId, EventCreateDto eventCreateDto) {
        if (userRepository.existsById(userId)) {
            User user = userRepository.findById(userId).get();
            Category category;
            if (eventCreateDto.getCategoryId() != null) {
                category = categoryRepository.findById((long) eventCreateDto.getCategoryId()).orElseThrow(() -> new CategoryNotFoundException(eventCreateDto.getCategoryId()));
            } else {
                category = Category.builder()
                        .id(999L)
                        .name("unnamed")
                        .build();
            }
            Location location;
            try {
                location = locationRepository.save(LocationMapper.toLocationForSaving(eventCreateDto.getLocation()));
            } catch (DataIntegrityViolationException e) {
                location = locationRepository.getLocationByLatAndLon(eventCreateDto.getLocation().getLat(), eventCreateDto.getLocation().getLon());
            }
            Event event = EventMapper.toEvent(eventCreateDto, user, category, location);
            if (event.getEventDate().isBefore(LocalDateTime.now()))
                throw new EventDateInPastException("Event date is in past!");
            return EventMapper.toEventFullDto(eventRepository.save(event));
        } else throw new UserNotFoundException(userId);
    }

    @Override
    public List<EventShortDto> getEventsByOwner(long userId, int from, int size) {
        if (userRepository.existsById(userId)) {
            PageRequest pageRequest = PageRequest.of(from / size, size);
            return eventRepository.findByOwner(userId, pageRequest).stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
        } else throw new UserNotFoundException(userId);
    }

    @Override
    public EventFullDto getEventByIdByOwner(long eventId, long userId) {
        if (userRepository.existsById(userId)) {
            if (eventRepository.existsById(eventId)) {
                return EventMapper.toEventFullDto(eventRepository.findByOwnerAndId(userId, eventId));
            } else throw new EventNotFoundException(eventId);
        } else throw new UserNotFoundException(userId);
    }

    @Override
    public EventFullDto updateEventByUser(long userId, long eventId, EventUserUpdateDto eventUserUpdateDto) {
        if (userRepository.existsById(userId)) {
            if (eventRepository.existsById(eventId)) {
                Event event = eventRepository.findByOwnerAndId(userId, eventId);
                if (event.getState() == EventState.PUBLISHED && eventUserUpdateDto.getStateAction() == null)
                    throw new EventAlreadyPublishedException("Cannot update published event!");
                if (!(eventUserUpdateDto.getCategoryId() == null)) {
                    Category category = categoryRepository.findById((long) eventUserUpdateDto.getCategoryId()).orElseThrow(() -> new CategoryNotFoundException(eventUserUpdateDto.getCategoryId()));
                    event.setCategory(category);
                }
                if (!(eventUserUpdateDto.getLocation() == null)) {
                    Location location;
                    try {
                        location = locationRepository.save(LocationMapper.toLocationForSaving(eventUserUpdateDto.getLocation()));
                    } catch (DataIntegrityViolationException e) {
                        location = locationRepository.getLocationByLatAndLon(eventUserUpdateDto.getLocation().getLat(), eventUserUpdateDto.getLocation().getLon());
                    }
                    event.setLocation(location);
                }
                if (!(eventUserUpdateDto.getAnnotation() == null)) {
                    event.setAnnotation(eventUserUpdateDto.getAnnotation());
                }
                if (!(eventUserUpdateDto.getDescription() == null)) {
                    event.setDescription(eventUserUpdateDto.getDescription());
                }
                if (eventUserUpdateDto.getEventDate() != null) {
                    event.setEventDate(LocalDateTime.from(CustomDateTimeFormatter.dateTimeFormatter.parse(eventUserUpdateDto.getEventDate())));
                    if (event.getEventDate().isBefore(LocalDateTime.now()))
                        throw new EventDateInPastException("Event date is in past!");
                }
                if (eventUserUpdateDto.getPaid() != null) {
                    event.setPaid(eventUserUpdateDto.getPaid());
                }
                if (!(eventUserUpdateDto.getParticipantLimit() == null)) {
                    event.setParticipantLimit(eventUserUpdateDto.getParticipantLimit());
                }
                if (eventUserUpdateDto.getRequestModeration() != null) {
                    event.setRequestModeration(eventUserUpdateDto.getRequestModeration());
                }
                if (eventUserUpdateDto.getStateAction() != null) {
                    switch (eventUserUpdateDto.getStateAction()) {
                        case CANCEL_REVIEW:
                            event.setState(EventState.CANCELED);
                            break;
                        case SEND_TO_REVIEW:
                            event.setState(EventState.PENDING);
                            break;
                    }
                }
                if (!(eventUserUpdateDto.getTitle() == null)) {
                    event.setTitle(eventUserUpdateDto.getTitle());
                }
                return EventMapper.toEventFullDto(eventRepository.save(event));
            } else throw new EventNotFoundException(eventId);
        } else throw new UserNotFoundException(userId);
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(List<Integer> users, List<Integer> categories, List<String> states, String rangeStart, String rangeEnd, Integer from, Integer size) {
        if (users != null && !users.isEmpty()) {
            users.forEach((userId) -> {
                if (!userRepository.existsById(Long.valueOf(userId))) throw new UserNotFoundException(userId);
            });
        }
        if (categories != null && !categories.isEmpty()) {
            categories.forEach((catId) -> {
                if (!categoryRepository.existsById(Long.valueOf(catId))) throw new CategoryNotFoundException(catId);
            });
        }
        PageRequest pageRequest = PageRequest.of(from / size, size);
        LocalDateTime start;
        if (rangeStart == null) {
            start = LocalDateTime.of(2000, 12, 12, 0, 0);
        } else {
            start = LocalDateTime.from(CustomDateTimeFormatter.dateTimeFormatter.parse(rangeStart));
        }
        LocalDateTime end;
        if (rangeEnd != null) {

            end = LocalDateTime.from(CustomDateTimeFormatter.dateTimeFormatter.parse(rangeEnd));
        } else {
            end = LocalDateTime.of(3000, 12, 12, 0, 0);
        }
        if (start.isAfter(end)) throw new StartAfterEndException("Start before end");
        List<Long> userIds;
        if (users != null && !users.isEmpty()) {
            userIds = users.stream().map(Integer::longValue).collect(Collectors.toList());
        } else {

            userIds = userRepository.returnListOfIds();
        }
        List<Long> categoryIds;
        if (categories != null && !categories.isEmpty()) {

            categoryIds = categories.stream().map(Integer::longValue).collect(Collectors.toList());
        } else {
            categoryIds = categoryRepository.returnListOfIds();
        }
        List<EventState> eventStates;
        if (states != null && !states.isEmpty()) {
            eventStates = states.stream().map(EventState::valueOf).collect(Collectors.toList());
        } else {
            eventStates = Arrays.stream(EventState.values()).collect(Collectors.toList());
        }
        return eventRepository.findEventsByAdmin(userIds, categoryIds, eventStates, start, end, pageRequest).stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());

    }


    @Override
    public EventFullDto updateEventByAdmin(long eventId, EventAdminUpdateDto eventAdminUpdateDto) {
        if (eventRepository.existsById(eventId)) {
            Event event = eventRepository.findById(eventId).get();
            if (!(eventAdminUpdateDto.getCategoryId() == null)) {
                Category category = categoryRepository.findById((long) eventAdminUpdateDto.getCategoryId()).orElseThrow(() -> new CategoryNotFoundException(eventAdminUpdateDto.getCategoryId()));
                event.setCategory(category);
            }
            if (!(eventAdminUpdateDto.getLocation() == null)) {
                Location location;
                try {
                    location = locationRepository.save(LocationMapper.toLocationForSaving(eventAdminUpdateDto.getLocation()));
                } catch (DataIntegrityViolationException e) {
                    location = locationRepository.getLocationByLatAndLon(eventAdminUpdateDto.getLocation().getLat(), eventAdminUpdateDto.getLocation().getLon());
                }
                event.setLocation(location);
            }
            if (!(eventAdminUpdateDto.getAnnotation() == null)) {
                event.setAnnotation(eventAdminUpdateDto.getAnnotation());
            }
            if (!(eventAdminUpdateDto.getDescription() == null)) {
                event.setDescription(eventAdminUpdateDto.getDescription());
            }
            if (eventAdminUpdateDto.getEventDate() != null) {
                event.setEventDate(LocalDateTime.from(CustomDateTimeFormatter.dateTimeFormatter.parse(eventAdminUpdateDto.getEventDate())));
                if (event.getEventDate().isBefore(LocalDateTime.now()))
                    throw new EventDateInPastException("Event date is in past!");
            }
            if (eventAdminUpdateDto.getPaid() != null) {
                event.setPaid(eventAdminUpdateDto.getPaid());
            }
            if (!(eventAdminUpdateDto.getParticipantLimit() == null)) {
                event.setParticipantLimit(eventAdminUpdateDto.getParticipantLimit());
            }
            if (eventAdminUpdateDto.getRequestModeration() != null) {
                event.setRequestModeration(eventAdminUpdateDto.getRequestModeration());
            }
            if (eventAdminUpdateDto.getStateAction() != null) {
                if (event.getState() != EventState.PUBLISHED) {
                    switch (eventAdminUpdateDto.getStateAction()) {
                        case REJECT_EVENT:
                            event.setState(EventState.CANCELED);
                            break;
                        case PUBLISH_EVENT:
                            if (event.getState() == EventState.CANCELED)
                                throw new EventAlreadyPublishedException("Event is canceled!");
                            event.setState(EventState.PUBLISHED);
                            event.setPublishedOn(LocalDateTime.now());
                            break;

                    }
                } else throw new EventAlreadyPublishedException("Event already published");
            }
            if (!(eventAdminUpdateDto.getTitle() == null)) {
                event.setTitle(eventAdminUpdateDto.getTitle());
            }
            return EventMapper.toEventFullDto(eventRepository.save(event));
        } else throw new EventNotFoundException(eventId);
    }

    @Override
    public List<EventShortDto> getPublishedEvents(String text, List<Integer> categories, String rangeStart, String rangeEnd, Boolean paid, boolean onlyAvailable, String sort, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        LocalDateTime start;
        if (rangeStart == null) {
            start = LocalDateTime.now();
        } else {
            start = LocalDateTime.from(CustomDateTimeFormatter.dateTimeFormatter.parse(rangeStart));
        }
        LocalDateTime end;
        if (rangeEnd != null) {

            end = LocalDateTime.from(CustomDateTimeFormatter.dateTimeFormatter.parse(rangeEnd));
        } else {
            end = LocalDateTime.of(3000, 12, 12, 0, 0);
        }
        if (start.isAfter(end)) throw new StartAfterEndException("Start before end");
        List<Long> categoryIds;
        if (categories != null && !categories.isEmpty()) {
            categoryIds = categories.stream().filter(Objects::nonNull).map(Integer::longValue).collect(Collectors.toList());
        } else {
            categoryIds = categoryRepository.returnListOfIds();
        }
        List<Event> unsortedEvents;
        if (!categoryIds.isEmpty()) {
            if (text != null) {
                log.warn(categoryIds.toString());
                unsortedEvents = eventRepository.findEventsByText(text.toLowerCase(), categoryIds, start, end, pageRequest);
            } else {
                log.warn(categoryIds.toString());
                unsortedEvents = eventRepository.findEventsWithoutText(categoryIds, start, end, pageRequest);
            }
        } else {
            if (text != null) {
                log.warn(categoryIds.toString());
                unsortedEvents = eventRepository.findEventsByTextWithoutCategories(text.toLowerCase(), start, end, pageRequest);
            } else {
                log.warn(categoryIds.toString());
                unsortedEvents = eventRepository.findEventsWithoutTextWithoutCategories(start, end, pageRequest);
            }
        }
        if (onlyAvailable) {
            unsortedEvents = unsortedEvents.stream().filter(event -> event.getConfirmedRequests() < event.getParticipantLimit()).collect(Collectors.toList());
        }
        if (paid != null) {
            if (paid) {
                unsortedEvents = unsortedEvents.stream().filter(Event::isPaid).collect(Collectors.toList());
            } else {
                unsortedEvents = unsortedEvents.stream().filter(event -> !event.isPaid()).collect(Collectors.toList());
            }
        }

        if (sort != null) {
            if (sort.equals("EVENT_DATE")) {
                return unsortedEvents.stream().sorted(Comparator.comparing(Event::getEventDate)).map(EventMapper::toEventShortDto).collect(Collectors.toList());
            }
            if (sort.equals("VIEWS")) {
                return unsortedEvents.stream().sorted(Comparator.comparing(Event::getViews)).map(EventMapper::toEventShortDto).collect(Collectors.toList());
            }
        }
        return unsortedEvents.stream().sorted(Comparator.comparing(Event::getId)).map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getState().equals(EventState.PUBLISHED)) {
            List<String> uris = new ArrayList<>();
            uris.add("/events/" + eventId);
            log.error(uris.toString());
            List<ViewStatsDto> viewStatsDtos = statsClient.getStats(LocalDateTime.of(2000, 12, 12, 12, 12), LocalDateTime.now().plusDays(1), uris, true);
            log.error(viewStatsDtos.toString());
            if (!viewStatsDtos.isEmpty()) {
                log.error(String.valueOf(viewStatsDtos.get(0).getHits().intValue()));
                event.setViews(viewStatsDtos.get(0).getHits().intValue());
                eventRepository.save(event);
            }
            return EventMapper.toEventFullDto(event);
        } else throw new EventNotAvailableException("Event must be published");
    }

    @Override
    public EventFullDto cancelEventByUser(long userId, long eventId) {
        if (userRepository.existsById(userId)) {
            Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
            event.setState(EventState.CANCELED);
            return EventMapper.toEventFullDto(eventRepository.save(event));
        } else throw new UserNotFoundException(userId);
    }

    @Override
    public AnswerRequestResultDto answerRequests(long userId, long eventId, RequestsAnswerDto answerDto) {
        log.warn(answerDto.toString());
        if (userRepository.existsById(userId)) {
            Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
            if (event.getConfirmedRequests() >= event.getParticipantLimit() && answerDto.getStatus() == RequestAnswerState.CONFIRMED)
                throw new EventLimitExceededException("Event participants limit exceeded");
            if (event.isRequestModeration()) {
                List<Request> requests = new ArrayList<>();
                answerDto.getRequestIds().forEach(id -> requests.add(requestRepository.findById(id.longValue()).orElseThrow(() -> new RequestNotFoundException(id.longValue()))));
                if ((requests.stream().anyMatch(request -> request.getStatus() == RequestStatus.CONFIRMED)) &&
                        answerDto.getStatus() == RequestAnswerState.REJECTED)
                    throw new RequestAlreadyConfirmedException("Request already confirmed: cant reject.");
                int i = 0;
                AnswerRequestResultDto answerRequestResultDto = AnswerRequestResultDto.builder()
                        .confirmedRequests(new ArrayList<>())
                        .rejectedRequests(new ArrayList<>())
                        .build();
                while ((event.getParticipantLimit() > event.getConfirmedRequests()) && i < requests.size()) {
                    if (answerDto.getStatus().equals(RequestAnswerState.CONFIRMED)) {
                        requests.get(i).setStatus(RequestStatus.CONFIRMED);
                    } else {
                        requests.get(i).setStatus(RequestStatus.REJECTED);
                    }
                    requestRepository.save(requests.get(i));
                    if (answerDto.getStatus() == RequestAnswerState.CONFIRMED) {
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                        answerRequestResultDto.getConfirmedRequests().add(RequestMapper.toRequestDto(requests.get(i)));
                    } else {
                        answerRequestResultDto.getRejectedRequests().add(RequestMapper.toRequestDto(requests.get(i)));
                    }
                    i++;
                }
                while (i < requests.size() && i != 0) {
                    requests.get(i).setStatus(RequestStatus.REJECTED);
                    requestRepository.save(requests.get(i));
                    answerRequestResultDto.getRejectedRequests().add(RequestMapper.toRequestDto(requests.get(i)));
                    i++;
                }
                eventRepository.save(event);
                log.warn(answerRequestResultDto.toString());
                return answerRequestResultDto;
            } else throw new EventLimitExceededException("Event is not moderated");
        } else throw new UserNotFoundException(userId);
    }

    @Override
    public List<RequestDto> getRequests(long userId, long eventId) {
        if (userRepository.existsById(userId)) {
            Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
            if (event.getInitiator().getId() == userId) {
                return requestRepository.findByEventId(eventId).stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
            } else throw new EventNotAvailableException("Not your event!");
        } else throw new UserNotFoundException(userId);
    }
}
