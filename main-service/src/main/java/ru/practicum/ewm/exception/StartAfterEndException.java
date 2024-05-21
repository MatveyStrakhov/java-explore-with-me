package ru.practicum.ewm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StartAfterEndException extends RuntimeException {
    public StartAfterEndException(String message) {
        super(message);
    }
}
