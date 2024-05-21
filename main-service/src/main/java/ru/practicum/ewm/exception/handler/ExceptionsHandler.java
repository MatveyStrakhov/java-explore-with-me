package ru.practicum.ewm.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.ewm.exception.*;
import ru.practicum.ewm.formatter.CustomDateTimeFormatter;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(value = {UserNotFoundException.class, CategoryNotFoundException.class, CompilationNotFoundException.class,
            EventNotFoundException.class, RequestNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .reason(e.getReason())
                .status(HttpStatus.NOT_FOUND)
                .timestamp(CustomDateTimeFormatter.dateTimeFormatter.format(LocalDateTime.now()))
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .reason("Incorrectly made request.")
                .timestamp(CustomDateTimeFormatter.dateTimeFormatter.format(LocalDateTime.now()))
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EventNotAvailableException.class})
    public ResponseEntity<Object> handleEventNotAvailableException(EventNotAvailableException e) {
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .reason("Event not available.")
                .timestamp(CustomDateTimeFormatter.dateTimeFormatter.format(LocalDateTime.now()))
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {CategoryIsUsedException.class, EventAlreadyPublishedException.class,
            DataIntegrityViolationException.class, RequestFromInitiatorException.class, EventNotPublishedException.class,
            EventLimitExceededException.class, RequestAlreadyConfirmedException.class})
    public ResponseEntity<Object> handleConflictExceptions(RuntimeException e) {
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .reason("For the requested operation the conditions are not met.")
                .timestamp(CustomDateTimeFormatter.dateTimeFormatter.format(LocalDateTime.now()))
                .status(HttpStatus.CONFLICT)
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {StartAfterEndException.class, EventDateInPastException.class, ConstraintViolationException.class})
    public ResponseEntity<Object> handleStartAfterEndException(RuntimeException e) {
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .reason("Incorrectly made request.")
                .timestamp(CustomDateTimeFormatter.dateTimeFormatter.format(LocalDateTime.now()))
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

}
