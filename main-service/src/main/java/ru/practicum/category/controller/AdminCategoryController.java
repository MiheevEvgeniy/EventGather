package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {
    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Запрос addCategory начат с телом {}", newCategoryDto);
        CategoryDto result = service.addCategory(newCategoryDto);
        log.info("Результат {}", result);
        return result;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        log.info("Запрос deleteCategory начат с id {}", catId);
        service.deleteCategory(catId);
        log.info("Запрос deleteCategory завершен");
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable long catId,
                                      @RequestBody CategoryDto body) {
        log.info("Запрос updateCategory начат с телом {} и id {}", body, catId);
        CategoryDto result = service.updateCategory(catId, body);
        log.info("Результат {}", result);
        return result;
    }
}
