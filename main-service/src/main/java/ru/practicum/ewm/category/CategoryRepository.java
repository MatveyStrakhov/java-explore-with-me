package ru.practicum.ewm.category;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.model.CategoryDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select new ru.practicum.ewm.category.model.CategoryDto(c.id, c.name) from Category c order by c.id")
    List<Optional<CategoryDto>> getCategoriesByPageAndReturnCategoryDto(PageRequest pageRequest);

    @Query("select c.id from Category c")
    List<Long> returnListOfIds();
}
