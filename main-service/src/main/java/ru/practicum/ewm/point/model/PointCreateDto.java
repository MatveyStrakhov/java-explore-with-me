package ru.practicum.ewm.point.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointCreateDto {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private Float lat;
    @NotNull
    private Float lon;
    @NotNull
    private Integer radius;
}
