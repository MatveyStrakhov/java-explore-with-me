package ru.practicum.ewm.stats.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@ConfigurationProperties(prefix = "url")
public class StatsClient {
    private String baseUrl = "http://stats-server:9090";

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private final WebClient webClient = WebClient.create(getBaseUrl());
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Void hit(String app, String uri, String ip, LocalDateTime timestamp) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(dateTimeFormatter.format(timestamp))
                .build();
        Mono<Void> response = webClient
                .post()
                .uri("/hit")
                .body(Mono.just(endpointHitDto), EndpointHitDto.class)
                .retrieve()
                .bodyToMono(Void.class);
        return response.block();
    }

    public List<ViewStatsDto> getStats(LocalDateTime startTime, LocalDateTime endTime, List<String> uris, Boolean unique) {
        String start = dateTimeFormatter.format(startTime);
        String end = dateTimeFormatter.format(endTime);
        StringBuilder builder = new StringBuilder();
        uris.forEach(uri -> builder.append("&uris=").append(uri));
        String listedUris = builder.toString();
        Mono<List<ViewStatsDto>> response = webClient
                .get()
                .uri("/stats?" + "start=" + start + "&end=" + end + listedUris + "&unique=" + unique)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ViewStatsDto>>() {
                });
        return response.block();
    }
}
