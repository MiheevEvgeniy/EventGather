package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.enums.States;
import ru.practicum.event.model.EventLocation;
import ru.practicum.user.dto.user.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class EventFullDto {
    @NotNull
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    private int confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    @NotNull
    private LocalDateTime eventDate;
    private LocalDateTime publishedOn;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private EventLocation location;
    private boolean paid;
    private int participantLimit;
    @Builder.Default
    private boolean requestModeration = true;
    private States state;
    private long views;
}