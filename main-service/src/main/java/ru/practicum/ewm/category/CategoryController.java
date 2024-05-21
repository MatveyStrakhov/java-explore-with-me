package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.model.CategoryDto;
import ru.practicum.ewm.category.model.CategoryRequestDto;
import ru.practicum.ewm.category.model.CategoryUpdateDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(required = false, value = "from", defaultValue = "0") Integer from,
                                           @RequestParam(required = false, value = "size", defaultValue = "10") Integer size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PositiveOrZero @NotNull @PathVariable long catId) {
        return categoryService.getCategoryById(catId);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<?> createCategoryByAdmin(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategoryByAdmin(categoryRequestDto));
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto updateCategoryByAdmin(@PositiveOrZero @NotNull @PathVariable long catId, @Valid @RequestBody CategoryUpdateDto categoryUpdateDto) {
        return categoryService.updateCategoryByAdmin(catId, categoryUpdateDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public ResponseEntity<?> deleteCategoryByAdmin(@PositiveOrZero @NotNull @PathVariable long catId) {
        categoryService.deleteCategoryByAdmin(catId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
