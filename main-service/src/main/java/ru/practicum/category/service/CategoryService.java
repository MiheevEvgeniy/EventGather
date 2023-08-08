package ru.practicum.category.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.category.dto.CategoryDto;

import javax.validation.constraints.Min;
import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategories(int from,
                                    int size);
    CategoryDto getCategoryById(long catId);
}