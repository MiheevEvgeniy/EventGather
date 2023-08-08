package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.repository.CategoryRepository;

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
        PagedListHolder page = new PagedListHolder(repository.findAll()
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
}
