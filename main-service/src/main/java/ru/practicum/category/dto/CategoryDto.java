package ru.practicum.category.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class CategoryDto {
    @NotNull
    private Long id;
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}