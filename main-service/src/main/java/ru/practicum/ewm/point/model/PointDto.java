package ru.practicum.ewm.point.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointDto {
    private Long id;
    private String name;
    private float lat;
    private float lon;
    private int radius;
}
