package ru.practicum.admin.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.enums.EventStateAction;
import ru.practicum.event.model.EventLocation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class UpdateEventAdminDto {
    private String title;
    private String annotation;
    private long category;
    private String description;
    private LocalDateTime eventDate;
    private EventLocation location;
    private Boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private EventStateAction stateAction;
}