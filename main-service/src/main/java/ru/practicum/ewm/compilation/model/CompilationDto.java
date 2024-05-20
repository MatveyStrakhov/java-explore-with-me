package ru.practicum.ewm.compilation.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.event.model.EventShortDto;

import java.util.List;

@Data
@Builder
public class CompilationDto {
    private long id;
    private List<EventShortDto> events;
    private boolean pinned;
    private String title;
}
