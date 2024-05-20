package ru.practicum.ewm.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventNotFoundException extends NotFoundException {
    public EventNotFoundException(long eventId) {
        super("Event with id=" + eventId + " was not found");
    }
}
