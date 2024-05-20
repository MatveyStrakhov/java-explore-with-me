package ru.practicum.ewm.event.model;

import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.model.CategoryMapper;
import ru.practicum.ewm.event.model.location.Location;
import ru.practicum.ewm.event.model.location.LocationMapper;
import ru.practicum.ewm.formatter.CustomDateTimeFormatter;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.model.UserMapper;

import java.time.LocalDateTime;

public class EventMapper {
    public static EventFullDto toEventFullDto(Event event) {
        String published;
        if (event.getPublishedOn() == null) {
            published = null;
        } else {
            published = CustomDateTimeFormatter.dateTimeFormatter.format(event.getPublishedOn());
        }
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .title(event.getTitle())
                .eventDate(CustomDateTimeFormatter.dateTimeFormatter.format(event.getEventDate()))
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(CustomDateTimeFormatter.dateTimeFormatter.format(event.getCreatedOn()))
                .description(event.getDescription())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .state(event.getState())
                .views(event.getViews())
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(published)
                .requestModeration(event.isRequestModeration())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .title(event.getTitle())
                .eventDate(CustomDateTimeFormatter.dateTimeFormatter.format(event.getEventDate()))
                .paid(event.isPaid())
                .views(event.getViews())
                .confirmedRequests(event.getConfirmedRequests())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .build();
    }

    public static Event toEvent(EventCreateDto eventCreateDto, User user, Category category, Location location) {
        return Event.builder()
                .annotation(eventCreateDto.getAnnotation())
                .initiator(user)
                .eventDate(LocalDateTime.from(CustomDateTimeFormatter.dateTimeFormatter.parse(eventCreateDto.getEventDate())))
                .confirmedRequests(0)
                .createdOn(LocalDateTime.now())
                .paid(eventCreateDto.isPaid())
                .publishedOn(null)
                .state(EventState.PENDING)
                .title(eventCreateDto.getTitle())
                .views(0)
                .description(eventCreateDto.getDescription())
                .location(location)
                .participantLimit(eventCreateDto.getParticipantLimit())
                .requestModeration(eventCreateDto.isRequestModeration())
                .category(category)
                .compilations(null)
                .build();
    }

}
