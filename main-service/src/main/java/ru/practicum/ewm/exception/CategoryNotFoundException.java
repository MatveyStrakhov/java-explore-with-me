package ru.practicum.ewm.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(long categoryId) {
        super("Category with id=" + categoryId + " was not found");
    }
}
