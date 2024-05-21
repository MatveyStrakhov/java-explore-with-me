package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.model.*;
import ru.practicum.ewm.event.model.AnswerRequestResultDto;
import ru.practicum.ewm.request.model.RequestDto;
import ru.practicum.ewm.stats.client.StatsClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventService eventService;
    private final StatsClient statsClient;

    @PostMapping(path = "/users/{userId}/events")
    public ResponseEntity<?> createEvent(@PathVariable long userId, @Valid @RequestBody EventCreateDto eventCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(userId, eventCreateDto));
    }

    @GetMapping(path = "/users/{userId}/events")
    public List<EventShortDto> getEventsByOwner(@PathVariable long userId, @PositiveOrZero @RequestParam(required = false, value = "from", defaultValue = "0") Integer from,
                                                @RequestParam(required = false, value = "size", defaultValue = "10") Integer size) {
        return eventService.getEventsByOwner(userId, from, size);
    }

    @GetMapping(path = "/users/{userId}/events/{eventId}")
    public EventFullDto getEventByIdByOwner(@PathVariable long userId, @PathVariable long eventId) {
        return eventService.getEventByIdByOwner(eventId, userId);
    }

    @PatchMapping(path = "/users/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable long userId, @PathVariable long eventId, @Valid @RequestBody EventUserUpdateDto eventUserUpdateDto) {
        return eventService.updateEventByUser(userId, eventId, eventUserUpdateDto);
    }

    @GetMapping(path = "/users/{userId}/events/{eventId}/requests")
    public List<RequestDto> getRequestsForEvent(@PathVariable long userId, @PathVariable long eventId) {
        return eventService.getRequests(userId, eventId);
    }

    @PatchMapping(path = "/users/{userId}/events/{eventId}/requests")
    public AnswerRequestResultDto answerRequestsForEvent(@PathVariable long userId, @PathVariable long eventId, @RequestBody RequestsAnswerDto answerDto) {
        return eventService.answerRequests(userId, eventId, answerDto);
    }

    @PatchMapping(path = "/users/{userId}/events/{eventId}/cancel")
    public EventFullDto cancelEvent(@PathVariable long userId, @PathVariable long eventId) {
        return eventService.cancelEventByUser(userId, eventId);
    }

    @GetMapping(path = "/admin/events")
    public List<EventFullDto> getEventsByAdmin(@RequestParam(required = false) List<Integer> users, @RequestParam(required = false) List<Integer> categories,
                                               @RequestParam(required = false) List<String> states,
                                               @RequestParam(required = false) String rangeStart,
                                               @RequestParam(required = false) String rangeEnd,
                                               @PositiveOrZero @RequestParam(required = false, value = "from", defaultValue = "0") Integer from,
                                               @RequestParam(required = false, value = "size", defaultValue = "10") Integer size) {
        return eventService.getEventsByAdmin(users, categories, states, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(path = "/admin/events/{eventId}")
    public EventFullDto updateEventByAdmin(@NotNull @PathVariable long eventId, @Valid @RequestBody EventAdminUpdateDto eventAdminUpdateDto) {
        return eventService.updateEventByAdmin(eventId, eventAdminUpdateDto);
    }

    @GetMapping(path = "/events")
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text, @RequestParam(required = false) List<Integer> categories,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false, value = "onlyAvailable", defaultValue = "false") boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @PositiveOrZero @RequestParam(required = false, value = "from", defaultValue = "0") Integer from,
                                         @RequestParam(required = false, value = "size", defaultValue = "10") Integer size, HttpServletRequest request) {
        statsClient.hit("ewm-main-app", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        return eventService.getPublishedEvents(text, categories, rangeStart, rangeEnd, paid, onlyAvailable, sort, from, size);
    }

    @GetMapping(path = "/events/{eventId}")
    public EventFullDto getEventById(@PathVariable long eventId, HttpServletRequest request) {
        statsClient.hit("ewm-main-app", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        return eventService.getEventById(eventId);
    }

}
