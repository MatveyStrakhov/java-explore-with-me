package ru.practicum.ewm.compilation.model;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventMapper;
import ru.practicum.ewm.event.model.EventShortDto;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation) {
        List<EventShortDto> eventIds = compilation.getEvents().stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .events(eventIds)
                .build();
    }

    public static Compilation toCompilation(CompilationCreateDto compilationDto, List<Event> events) {
        return Compilation.builder()
                .title(compilationDto.getTitle())
                .pinned(compilationDto.isPinned())
                .events(events)
                .build();
    }
}
