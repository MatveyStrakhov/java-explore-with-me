package ru.practicum.ewm.stats.server;

import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.ewm.stats.dto.ViewStatsRequestDto;

import java.util.List;

public interface StatsServerService {
    List<ViewStatsDto> getStats(ViewStatsRequestDto viewStatsRequestDto);

    EndpointHitDto receiveEndpointHit(EndpointHitDto endpointHitDto);
}
