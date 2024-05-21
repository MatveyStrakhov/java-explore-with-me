package ru.practicum.ewm.event.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.category.model.CategoryDto;
import ru.practicum.ewm.event.model.location.LocationDto;
import ru.practicum.ewm.user.model.UserShortDto;


@Data
@Builder
public class EventFullDto {
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private LocationDto location;
    private boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Integer views;
}
