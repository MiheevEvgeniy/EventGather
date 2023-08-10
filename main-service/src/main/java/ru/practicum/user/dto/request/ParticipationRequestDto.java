package ru.practicum.user.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.enums.States;

import java.time.LocalDateTime;

@Data
@Builder
public class ParticipationRequestDto {
    private long id;
    private LocalDateTime created;
    private long event;
    private long requester;
    private States status;
}