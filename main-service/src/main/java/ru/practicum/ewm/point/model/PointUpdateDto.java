package ru.practicum.ewm.point.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointUpdateDto {
    private String name;
    private Float lat;
    private Float lon;
    private Integer radius;
}
