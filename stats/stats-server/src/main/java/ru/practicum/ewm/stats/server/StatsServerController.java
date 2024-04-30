package ru.practicum.ewm.stats.server;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.ewm.stats.dto.ViewStatsRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsServerController {
    private final StatsServerService service;
    @PostMapping("/hit")
    public void receiveEndpointHit(@RequestBody EndpointHitDto endpointHitDto){
        service.receiveEndpointHit(endpointHitDto);
    }
    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam String start,  @RequestParam String end, @RequestParam(required = false) List<String> uris, @RequestParam(required = false) boolean unique){
        ViewStatsRequestDto viewStatsRequestDto = ViewStatsRequestDto.builder()
                .start(start)
                .end(end)
                .uris(uris)
                .unique(unique)
                .build();
        return service.getStats(viewStatsRequestDto);
    }
}
