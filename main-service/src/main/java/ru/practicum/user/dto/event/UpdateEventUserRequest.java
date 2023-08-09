package ru.practicum.user.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.enums.EventStateAction;
import ru.practicum.event.model.EventLocation;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateEventUserRequest {
    private String title;
    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    private EventLocation location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventStateAction stateAction;
}