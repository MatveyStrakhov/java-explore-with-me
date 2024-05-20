package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.model.CompilationCreateDto;
import ru.practicum.ewm.compilation.model.CompilationDto;
import ru.practicum.ewm.compilation.model.CompilationUpdateDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController()
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    @PostMapping("/admin/compilations")
    public ResponseEntity<?> createCompilation(@Valid @RequestBody CompilationCreateDto compilationCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compilationService.createCompilation(compilationCreateDto));
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId, @Valid @RequestBody CompilationUpdateDto compilationUpdateDto) {
        return compilationService.updateCompilation(compId, compilationUpdateDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public ResponseEntity<?> deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@PositiveOrZero @RequestParam(required = false, value = "from", defaultValue = "0") Integer from,
                                                @RequestParam(required = false, value = "size", defaultValue = "10") Integer size) {
        return compilationService.getAllCompilations(from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        return compilationService.getCompilationById(compId);
    }
}
