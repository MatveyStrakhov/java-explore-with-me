package ru.practicum.ewm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EventLimitExceededException extends RuntimeException {
    public EventLimitExceededException(String message) {
        super(message);
    }
}
