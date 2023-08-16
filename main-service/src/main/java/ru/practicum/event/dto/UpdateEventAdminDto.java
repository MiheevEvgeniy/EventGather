package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.enums.EventStateAction;
import ru.practicum.location.model.Location;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateEventAdminDto {
    private String title;
    private String annotation;
    private long category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private EventStateAction stateAction;
}