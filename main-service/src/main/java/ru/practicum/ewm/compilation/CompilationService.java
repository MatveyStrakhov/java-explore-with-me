package ru.practicum.ewm.compilation;

import ru.practicum.ewm.compilation.model.CompilationCreateDto;
import ru.practicum.ewm.compilation.model.CompilationDto;
import ru.practicum.ewm.compilation.model.CompilationUpdateDto;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(CompilationCreateDto compilationCreateDto);

    CompilationDto updateCompilation(Long compId, CompilationUpdateDto compilationUpdateDto);

    void deleteCompilation(Long compId);

    List<CompilationDto> getAllCompilations(Integer from, Integer size);

    CompilationDto getCompilationById(Long compId);
}
