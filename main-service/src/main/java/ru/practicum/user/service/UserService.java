package ru.practicum.user.service;

import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.event.NewEventDto;
import ru.practicum.user.dto.event.UpdateEventUserRequest;
import ru.practicum.user.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.user.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.user.dto.request.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

public interface UserService {
    //-------------Event-Requests--------------------
    List<ParticipationRequestDto> getUsersRequestsInfo(long userId);
    ParticipationRequestDto sendEventRequest(long userId, long eventId);
    ParticipationRequestDto cancelEventRequest(long userId, long requestId);
    //---------------Events-------------------------
    List<EventShortDto> getOwnedEvents(long userId, int from, int size);
    EventFullDto addEvent(long userId,
                          NewEventDto newEventDto);
    EventFullDto getEventInfo(long userId, long eventId);
    EventFullDto updateOwnedEvent(long userId, long eventId, UpdateEventUserRequest updBody);
    List<ParticipationRequestDto> getEventRequestsInfo(long userId, long eventId);
    EventRequestStatusUpdateResult updateEventRequestsStatus(long userId,
                                                             long eventId,
                                                             EventRequestStatusUpdateRequest updRequest);
}