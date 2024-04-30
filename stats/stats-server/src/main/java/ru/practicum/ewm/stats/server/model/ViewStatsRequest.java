package ru.practicum.ewm.stats.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewStatsRequest {
    private LocalDateTime start;
    private LocalDateTime end;
    private List<String> uris;
    private boolean unique;

}
