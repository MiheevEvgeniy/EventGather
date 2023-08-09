package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;


    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        PagedListHolder<CategoryDto> page = new PagedListHolder<>(repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList()));
        page.setPageSize(size);
        page.setPage(from);
        return new ArrayList<>(page.getPageList());
    }

    @Override
    public CategoryDto getCategoryById(long catId) {
        return mapper.toDto(repository.getById(catId));
    }

    //---------------------Admin------------------

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        return mapper.toDto(
                repository.save(mapper.toEntity(newCategoryDto)));
    }

    @Override
    public void deleteCategory(long catId) {
        Category category = repository.getById(catId);
        if (category == null) {
            throw new NotFoundException("Category with id =" + catId + " was not found");
        }
        repository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(long catId, CategoryDto body) {
        Category actualCategory = repository.getById(catId);
        Category updCategory = mapper.toEntity(body);
        if (!updCategory.getName().equals(actualCategory.getName())) {
            if (updCategory.getName().length() > 50) {
                throw new BadRequestException("Name of category is invalid");
            }
            actualCategory.setName(updCategory.getName());
        }
        return mapper.toDto(repository.save(actualCategory));
    }
}
