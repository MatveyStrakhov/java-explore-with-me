package ru.practicum.ewm.point;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.EventFullDto;
import ru.practicum.ewm.event.model.EventMapper;
import ru.practicum.ewm.exception.PointNotFoundException;
import ru.practicum.ewm.point.model.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    private final PointRepository pointRepository;
    private final EventRepository eventRepository;

    @Override
    public List<PointDto> returnAllPoints(Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return pointRepository.findAll(pageRequest).stream().map(PointMapper::toPointDto).collect(Collectors.toList());
    }

    @Override
    public PointDto returnPointById(long pointId) {
        return PointMapper.toPointDto(pointRepository.findById(pointId).orElseThrow(() -> new PointNotFoundException(pointId)));
    }

    @Override
    public PointDto updatePoint(long pointId, PointUpdateDto pointUpdateDto) {
        Point point = pointRepository.findById(pointId).orElseThrow(() -> new PointNotFoundException(pointId));
        if (pointUpdateDto.getLat() != null) {
            point.setLat(pointUpdateDto.getLat());
        }
        if (pointUpdateDto.getLon() != null) {
            point.setLon(pointUpdateDto.getLon());
        }
        if (pointUpdateDto.getName() != null) {
            point.setName(pointUpdateDto.getName());
        }
        if (pointUpdateDto.getRadius() != null) {
            point.setRadius(pointUpdateDto.getRadius());
        }
        return PointMapper.toPointDto(pointRepository.save(point));
    }

    @Override
    public void deletePointById(long pointId) {
        if (pointRepository.existsById(pointId)) {
            pointRepository.deleteById(pointId);
        } else throw new PointNotFoundException(pointId);

    }

    @Override
    public List<EventFullDto> getEventsByPoint(long pointId, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        Point point = pointRepository.findById(pointId).orElseThrow(() -> new PointNotFoundException(pointId));
        return eventRepository.findByPoint(point.getLat(),point.getLon(), point.getRadius(),pageRequest).stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @Override
    public PointDto createPoint(PointCreateDto pointCreateDto) {
        return PointMapper.toPointDto(pointRepository.save(PointMapper.toPoint(pointCreateDto)));
    }
}
