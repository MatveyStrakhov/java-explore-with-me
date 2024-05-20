package ru.practicum.ewm.event.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.event.model.location.LocationDto;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@Builder
public class EventAdminUpdateDto {
    @Size(min = 20, max = 2000)
    private String annotation;
    @JsonAlias(value = "category")
    private Integer categoryId;
    @Size(min = 20, max = 7000)
    private String description;
    private String eventDate;
    @JsonAlias(value = "location")
    private LocationDto location;
    @Builder.Default
    private Boolean paid = null;
    @PositiveOrZero
    private Integer participantLimit;
    @Builder.Default
    private Boolean requestModeration = null;
    private AdminEventUpdateAction stateAction;
    @Size(min = 3, max = 120)
    private String title;
}
