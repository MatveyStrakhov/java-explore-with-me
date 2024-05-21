package ru.practicum.ewm.stats.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class ExceptionsHandler {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(value = {DateTimeException.class})
    public ResponseEntity<Object> handleDateTimeException(DateTimeException e) {
        ApiError apiError = ApiError.builder()
                .message(e.getMessage())
                .reason("Incorrectly made request.")
                .timestamp(dateTimeFormatter.format(LocalDateTime.now()))
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
