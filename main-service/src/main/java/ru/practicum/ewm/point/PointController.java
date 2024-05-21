package ru.practicum.ewm.point;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.model.EventFullDto;
import ru.practicum.ewm.point.model.PointCreateDto;
import ru.practicum.ewm.point.model.PointDto;
import ru.practicum.ewm.point.model.PointUpdateDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/points")
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;

    @GetMapping()
    public List<PointDto> getPoints(@PositiveOrZero @RequestParam(required = false, value = "from", defaultValue = "0") Integer from,
                                    @RequestParam(required = false, value = "size", defaultValue = "10") Integer size) {
        return pointService.returnAllPoints(from, size);
    }

    @GetMapping("/{pointId}")
    public PointDto getPointById(@NotNull @PathVariable long pointId) {
        return pointService.returnPointById(pointId);
    }

    @PostMapping()
    public ResponseEntity<?> createPoint(@Valid @RequestBody PointCreateDto pointCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pointService.createPoint(pointCreateDto));
    }

    @PatchMapping("/{pointId}")
    public PointDto updatePointById(@NotNull @PathVariable long pointId, @Valid @RequestBody PointUpdateDto pointUpdateDto) {
        return pointService.updatePoint(pointId, pointUpdateDto);
    }

    @DeleteMapping("/{pointId}")
    public ResponseEntity<?> deletePointById(@NotNull @PathVariable long pointId) {
        pointService.deletePointById(pointId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{pointId}/events")
    public List<EventFullDto> getEventsByPoint(@NotNull @PathVariable long pointId, @PositiveOrZero @RequestParam(required = false, value = "from", defaultValue = "0") Integer from,
                                               @RequestParam(required = false, value = "size", defaultValue = "10") Integer size) {
        return pointService.getEventsByPoint(pointId, from, size);
    }


}
