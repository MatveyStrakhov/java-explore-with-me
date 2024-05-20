package ru.practicum.ewm.event.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.category.model.CategoryDto;
import ru.practicum.ewm.user.model.UserShortDto;

@Data
@Builder
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private Integer views;
}
