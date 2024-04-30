package ru.practicum.ewm.stats.server.model;

import ru.practicum.ewm.stats.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EndpointHitMapper {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .id(null)
                .ip(endpointHitDto.getIp())
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .timestamp(LocalDateTime.from(dateTimeFormatter.parse(endpointHitDto.getTimestamp())))
                .build();
    }
}
