package ru.practicum.ewm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventNotAvailableException extends RuntimeException {
    public EventNotAvailableException(String message) {
        super(message);
    }
}
