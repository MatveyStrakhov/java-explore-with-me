package ru.practicum.ewm.stats.server;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.ewm.stats.dto.ViewStatsRequestDto;
import ru.practicum.ewm.stats.server.model.EndpointHitMapper;
import ru.practicum.ewm.stats.server.model.ViewStatsRequest;
import ru.practicum.ewm.stats.server.model.ViewStatsRequestMapper;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StatsServerServiceImpl implements StatsServerService {
    private final StatsServerRepository repository;

    @Override
    public List<ViewStatsDto> getStats(ViewStatsRequestDto viewStatsRequestDto) {
        ViewStatsRequest viewStatsRequest = ViewStatsRequestMapper.toViewStatsRequest(viewStatsRequestDto);
        List<ViewStatsDto> viewStatsDtoList = new ArrayList<>();
        if (viewStatsRequest.getUris() != null && !(viewStatsRequest.getUris().isEmpty())) {
            for (String uri : viewStatsRequest.getUris()) {
                List<Optional<ViewStatsDto>> viewStatsDtoOptionalList;
                if (viewStatsRequest.isUnique()) {
                    viewStatsDtoOptionalList = repository.countDistinctIpByUriAndReturnViewStatsDto(uri, viewStatsRequest.getStart(), viewStatsRequest.getEnd());
                } else {
                    viewStatsDtoOptionalList = repository.countByUriAndReturnViewStatsDto(uri, viewStatsRequest.getStart(), viewStatsRequest.getEnd());
                }
                if (!viewStatsDtoOptionalList.isEmpty()) {
                    viewStatsDtoOptionalList.forEach((i) -> i.ifPresent(viewStatsDtoList::add));
                }
            }

            viewStatsDtoList.sort(Comparator.comparingLong(ViewStatsDto::getHits).reversed());
            return viewStatsDtoList;

        } else {
            List<Optional<ViewStatsDto>> viewStatsDtoOptionalList;
            if (viewStatsRequest.isUnique()) {
                viewStatsDtoOptionalList = repository.countHitsByDistinctIp(viewStatsRequest.getStart(), viewStatsRequest.getEnd());
            } else {
                viewStatsDtoOptionalList = repository.countHitsByIp(viewStatsRequest.getStart(), viewStatsRequest.getEnd());
            }
            if (!viewStatsDtoOptionalList.isEmpty()) {
                viewStatsDtoOptionalList.forEach((i) -> i.ifPresent(viewStatsDtoList::add));
            }
        }
        viewStatsDtoList.sort(Comparator.comparingLong(ViewStatsDto::getHits).reversed());
        return viewStatsDtoList;
    }

    @Override
    public void receiveEndpointHit(EndpointHitDto endpointHitDto) {
        repository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));

    }
}
