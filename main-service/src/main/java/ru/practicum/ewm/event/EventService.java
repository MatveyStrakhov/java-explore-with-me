package ru.practicum.ewm.event;

import ru.practicum.ewm.event.model.*;
import ru.practicum.ewm.request.model.RequestDto;

import java.util.List;

public interface EventService {
    EventFullDto createEvent(long userId, EventCreateDto eventCreateDto);

    List<EventShortDto> getEventsByOwner(long userId, int from, int size);

    EventFullDto getEventByIdByOwner(long eventId, long userId);

    EventFullDto updateEventByUser(long userId, long eventId, EventUserUpdateDto eventUserUpdateDto);

    List<EventFullDto> getEventsByAdmin(List<Integer> users, List<Integer> categories, List<String> states, String rangeStart, String rangeEnd, Integer from, Integer size);

    EventFullDto updateEventByAdmin(long eventId, EventAdminUpdateDto eventAdminUpdateDto);

    List<EventShortDto> getPublishedEvents(String text, List<Integer> categories, String rangeStart, String rangeEnd, Boolean paid, boolean onlyAvailable, String sort, Integer from, Integer size);

    EventFullDto getEventById(long eventId);

    EventFullDto cancelEventByUser(long userId, long eventId);

    AnswerRequestResultDto answerRequests(long userId, long eventId, RequestsAnswerDto answerDto);

    List<RequestDto> getRequests(long userId, long eventId);
}
