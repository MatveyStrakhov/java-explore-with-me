package ru.practicum.ewm.event.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.event.model.location.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@Builder
@Jacksonized
public class EventCreateDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @JsonAlias(value = "category")
    private Integer categoryId;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @NotBlank
    private String eventDate;
    @JsonAlias(value = "location")
    private LocationDto location;
    @Builder.Default
    private boolean paid = false;
    @PositiveOrZero
    @Builder.Default
    private int participantLimit = 0;
    @Builder.Default
    private boolean requestModeration = true;
    @Length(min = 3, max = 120)
    private String title;
}
