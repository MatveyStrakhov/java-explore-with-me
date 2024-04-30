package ru.practicum.ewm.stats.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.ewm.stats.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StatsServerRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select distinct new ru.practicum.ewm.stats.dto.ViewStatsDto(h.app, h.uri, count(distinct h.ip)) from EndpointHit h where h.uri like ?1 and h.timestamp > ?2 and h.timestamp < ?3 group by h.id, h.ip, h.uri")
    List<Optional<ViewStatsDto>> countDistinctIpByUriAndReturnViewStatsDto(String uri, LocalDateTime start, LocalDateTime end);
    @Query("select distinct new ru.practicum.ewm.stats.dto.ViewStatsDto(h.app, h.uri, count(h2.id)) from EndpointHit h join EndpointHit h2 on h.ip = h2.ip where h.uri like ?1 and h2.uri like h.uri and h.timestamp > ?2 and h.timestamp < ?3 group by h.id, h.ip, h.uri")
    List<Optional<ViewStatsDto>> countByUriAndReturnViewStatsDto(String uri, LocalDateTime start, LocalDateTime end);
    @Query("select distinct new ru.practicum.ewm.stats.dto.ViewStatsDto(h.app, h.uri, count(h2.id)) from EndpointHit h join EndpointHit h2 on h.ip = h2.ip where h.timestamp > ?1 and h.timestamp < ?2 and h2.uri like h.uri group by h.id, h.ip, h.uri")
    List<Optional<ViewStatsDto>> countHitsByIp(LocalDateTime start, LocalDateTime end);
    @Query("select distinct new ru.practicum.ewm.stats.dto.ViewStatsDto(h.app, h.uri, count(distinct h.ip)) from EndpointHit h where h.timestamp > ?1 and h.timestamp < ?2 group by h.id, h.ip, h.uri")
    List<Optional<ViewStatsDto>> countHitsByDistinctIp(LocalDateTime start, LocalDateTime end);
}
