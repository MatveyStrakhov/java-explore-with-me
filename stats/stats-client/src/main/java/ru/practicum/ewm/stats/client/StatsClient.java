package ru.practicum.ewm.stats.client;

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
public class StatsClient {
    private final WebClient webClient = WebClient.create("http://localhost:9090");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void hit(String app, String uri, String ip, LocalDateTime timestamp) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(dateTimeFormatter.format(timestamp))
                .build();
        webClient
                .post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(endpointHitDto, EndpointHitDto.class);
    }

    public List<ViewStatsDto> getStats(LocalDateTime startTime, LocalDateTime endTime, List<String> uris, Boolean unique) {
        String start = dateTimeFormatter.format(startTime);
        String end = dateTimeFormatter.format(endTime);
        Mono<List<ViewStatsDto>> response = webClient
                .get()
                .uri("/stats" + "start=" + start + "end=" + end + "uris=" + uris + "unique" + unique)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ViewStatsDto>>() {});
        return response.block();
    }
}
