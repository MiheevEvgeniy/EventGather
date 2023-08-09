package ru.practicum.user.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.event.model.EventLocation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class NewEventDto {
    @Size(min = 3, max = 120)
    @NotBlank
    private String title;
    @Size(min = 20, max = 2000)
    @NotBlank
    private String annotation;
    @NotNull
    private Long category;
    @Size(min = 20, max = 7000)
    @NotBlank
    private String description;
    @NotNull
    private LocalDateTime eventDate;
    @NotNull
    private EventLocation location;
    private boolean paid;
    private int participantLimit;
    @Builder.Default
    private Boolean requestModeration = true;
}