package ru.practicum.user.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateResult {
    @Builder.Default
    List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
    @Builder.Default
    List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
}