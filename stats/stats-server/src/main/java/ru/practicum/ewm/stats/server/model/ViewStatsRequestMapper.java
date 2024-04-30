package ru.practicum.ewm.stats.server.model;

import ru.practicum.ewm.stats.dto.ViewStatsRequestDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ViewStatsRequestMapper {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ViewStatsRequest toViewStatsRequest(ViewStatsRequestDto dto) {
        return ViewStatsRequest.builder()
                .start(LocalDateTime.from(dateTimeFormatter.parse(dto.getStart())))
                .end(LocalDateTime.from(dateTimeFormatter.parse(dto.getEnd())))
                .uris(dto.getUris())
                .unique(dto.isUnique())
                .build();
    }
}
