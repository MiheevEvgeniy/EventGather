package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.user.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class EventShortDto {
    @NotNull
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    private int confirmedRequests;
    @NotNull
    private LocalDateTime eventDate;
    @NotNull
    private UserShortDto initiator;
    private boolean paid;
    private long views;
}