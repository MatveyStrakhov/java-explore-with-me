package ru.practicum.ewm.category.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Jacksonized
public class CategoryRequestDto {
    @NotNull
    @NotBlank
    @Length(min = 1, max = 50)
    private String name;
}
