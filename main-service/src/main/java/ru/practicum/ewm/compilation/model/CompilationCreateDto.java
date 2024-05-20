package ru.practicum.ewm.compilation.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@Jacksonized
public class CompilationCreateDto {
    private List<Long> events;
    @Builder.Default
    private boolean pinned = false;
    @Size(min = 1, max = 50)
    @NotNull
    @NotBlank
    private String title;


}
