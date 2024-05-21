package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.model.*;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.CompilationNotFoundException;
import ru.practicum.ewm.exception.EventNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto createCompilation(CompilationCreateDto compilationCreateDto) {
        List<Event> events = new ArrayList<>();
        if (compilationCreateDto.getEvents() != null && !compilationCreateDto.getEvents().isEmpty()) {
            events = compilationCreateDto.getEvents().stream().map((Long eventId) -> eventRepository
                    .findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId))).collect(Collectors.toList());
        }
        return CompilationMapper.toCompilationDto(compilationRepository.save(CompilationMapper.toCompilation(compilationCreateDto, events)));
    }

    @Override
    public CompilationDto updateCompilation(Long compId, CompilationUpdateDto compilationUpdateDto) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new CompilationNotFoundException(compId));
        if (compilationUpdateDto.getTitle() != null) {
            compilation.setTitle(compilationUpdateDto.getTitle());
        }
        if (compilationUpdateDto.getPinned() != null) {
            compilation.setPinned(compilationUpdateDto.getPinned());
        }
        if (compilationUpdateDto.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllById(compilationUpdateDto.getEvents()));
        }
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));

    }

    @Override
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new CompilationNotFoundException(compId));
        compilation.getEvents().forEach(event -> event.setCompilations(event.getCompilations().stream()
                .filter(c -> !Objects.equals(c.getId(), compId)).collect(Collectors.toList())));
        compilation.getEvents().forEach(eventRepository::save);
        compilationRepository.deleteById(compId);
    }

    @Override
    public List<CompilationDto> getAllCompilations(Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return compilationRepository.findAll(pageRequest).stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        return CompilationMapper.toCompilationDto(compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId)));
    }
}
