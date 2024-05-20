package ru.practicum.ewm.category.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.Length;


@Data
@Builder
@Jacksonized
public class CategoryUpdateDto {
    @Length(min = 1, max = 50)
    private String name;
}
