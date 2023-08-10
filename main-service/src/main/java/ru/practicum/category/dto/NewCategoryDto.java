package ru.practicum.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NewCategoryDto {
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}