package ru.practicum.ewm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CategoryIsUsedException extends RuntimeException {
    public CategoryIsUsedException(String message) {
        super(message);
    }
}
