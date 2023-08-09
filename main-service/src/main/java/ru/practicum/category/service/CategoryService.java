package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategories(int from,
                                    int size);

    CategoryDto getCategoryById(long catId);
}