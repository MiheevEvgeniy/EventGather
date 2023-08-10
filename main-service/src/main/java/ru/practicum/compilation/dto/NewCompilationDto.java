package ru.practicum.compilation.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class NewCompilationDto {
    @Builder.Default
    private List<Long> events = new ArrayList<>();
    private boolean pinned;
    @Size(min = 1, max = 50)
    @NotBlank
    private String title;
}