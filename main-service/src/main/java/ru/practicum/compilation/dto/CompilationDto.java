package ru.practicum.compilation.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.event.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class CompilationDto {
    @NotNull
    private Long id;
    @Builder.Default
    private List<EventShortDto> events = new ArrayList<>();
    private boolean pinned;
    @Size(min = 1, max = 50)
    @NotBlank
    private String title;
}