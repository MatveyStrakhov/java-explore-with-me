package ru.practicum.ewm.point;

import ru.practicum.ewm.event.model.EventFullDto;
import ru.practicum.ewm.point.model.PointCreateDto;
import ru.practicum.ewm.point.model.PointDto;
import ru.practicum.ewm.point.model.PointUpdateDto;

import java.util.List;

public interface PointService {
    List<PointDto> returnAllPoints(Integer from, Integer size);

    PointDto returnPointById(long pointId);

    PointDto updatePoint(long pointId, PointUpdateDto pointUpdateDto);

    void deletePointById(long pointId);

    List<EventFullDto> getEventsByPoint(long pointId, Integer from, Integer size);

    PointDto createPoint(PointCreateDto pointCreateDto);
}
