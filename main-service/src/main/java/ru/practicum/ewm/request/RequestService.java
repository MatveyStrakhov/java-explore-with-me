package ru.practicum.ewm.request;

import ru.practicum.ewm.request.model.RequestDto;

import java.util.List;

public interface RequestService {
    List<RequestDto> getOwnRequests(long userId);

    RequestDto createRequest(long userId, long eventId);

    RequestDto cancelRequest(long userId, long requestId);
}
