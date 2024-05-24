package ru.practicum.ewm.exception;

public class PointNotFoundException extends NotFoundException {
    public PointNotFoundException(long pointId) {
        super("Point with id=" + pointId + " was not found");
    }
}
