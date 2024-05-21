package ru.practicum.ewm.category;

import ru.practicum.ewm.category.model.CategoryDto;
import ru.practicum.ewm.category.model.CategoryRequestDto;
import ru.practicum.ewm.category.model.CategoryUpdateDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(long catId);

    CategoryDto createCategoryByAdmin(CategoryRequestDto categoryRequestDto);

    CategoryDto updateCategoryByAdmin(long catId, CategoryUpdateDto categoryRequestDto);

    void deleteCategoryByAdmin(long catId);
}
