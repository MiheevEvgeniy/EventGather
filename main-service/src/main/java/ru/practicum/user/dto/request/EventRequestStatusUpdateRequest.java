package ru.practicum.user.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.enums.States;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateRequest {
    @Builder.Default
    private List<Long> requestIds = new ArrayList<>();
    private States status;
}