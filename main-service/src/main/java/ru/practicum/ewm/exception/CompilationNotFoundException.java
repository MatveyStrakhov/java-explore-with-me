package ru.practicum.ewm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CompilationNotFoundException extends NotFoundException {
    public CompilationNotFoundException(long compId) {
        super("Compilation with id=" + compId + " was not found");
    }
}
