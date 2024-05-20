package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.*;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.CategoryIsUsedException;
import ru.practicum.ewm.exception.CategoryNotFoundException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return categoryRepository.getCategoriesByPageAndReturnCategoryDto(pageRequest).stream()
                .filter(Optional::isPresent).map((Optional::get)).collect(Collectors.toList());

    }

    @Override
    public CategoryDto getCategoryById(long catId) {
        return CategoryMapper.toCategoryDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException(catId)));
    }

    @Override
    public CategoryDto createCategoryByAdmin(CategoryRequestDto categoryRequestDto) {
        log.warn(categoryRequestDto.toString());
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(categoryRequestDto)));
    }

    @Override
    public CategoryDto updateCategoryByAdmin(long catId, @Valid CategoryUpdateDto categoryUpdateDto) {
        if (categoryRepository.existsById(catId)) {
            Category category = categoryRepository.findById(catId).get();
            if (categoryUpdateDto.getName() != null) {
                category.setName(categoryUpdateDto.getName());
            }
            return CategoryMapper.toCategoryDto(categoryRepository.save(category));
        } else throw new CategoryNotFoundException(catId);
    }

    @Override
    public void deleteCategoryByAdmin(long catId) {
        if (categoryRepository.existsById(catId)) {
            if (eventRepository.findByCategoryId(catId).isEmpty()) {
                categoryRepository.deleteById(catId);
            } else throw new CategoryIsUsedException("The category is not empty");

        } else throw new CategoryNotFoundException(catId);

    }
}
