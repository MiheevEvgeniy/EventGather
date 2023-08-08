package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService service;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @Min(0) int from,
                                           @RequestParam(defaultValue = "10") @Min(1) int size){
        log.info("Запрос getCategories начат с параметрами from {} и size {}",from,size);
        List<CategoryDto> result = service.getCategories(from,size);
        log.info("Запрос завершен {}",result);
        return result;
    }
    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable long catId){
        return service.getCategoryById(catId);
    }
}
