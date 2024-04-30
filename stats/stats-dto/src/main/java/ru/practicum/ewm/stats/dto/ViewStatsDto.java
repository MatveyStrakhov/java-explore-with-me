package ru.practicum.ewm.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ViewStatsDto {
    private String app;
    private String uri;
    private Long hits;
}
