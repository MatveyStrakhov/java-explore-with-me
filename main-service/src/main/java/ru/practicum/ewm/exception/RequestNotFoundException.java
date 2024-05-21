package ru.practicum.ewm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RequestNotFoundException extends NotFoundException {

    public RequestNotFoundException(long requestId) {
        super("Request with id=" + requestId + " was not found");
    }
}
