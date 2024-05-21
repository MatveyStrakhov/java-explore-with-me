package ru.practicum.ewm.request.model;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.formatter.CustomDateTimeFormatter;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;

public class RequestMapper {
    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .status(request.getStatus())
                .created(CustomDateTimeFormatter.dateTimeFormatter.format(request.getCreated()))
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .build();
    }

    public static Request toRequest(RequestDto requestDto, User user, Event event) {
        return Request.builder()
                .id(requestDto.getId())
                .created(LocalDateTime.from(CustomDateTimeFormatter.dateTimeFormatter.parse(requestDto.getCreated())))
                .requester(user)
                .event(event)
                .status(requestDto.getStatus())
                .build();
    }
}
