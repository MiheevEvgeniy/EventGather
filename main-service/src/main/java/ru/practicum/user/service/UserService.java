package ru.practicum.user.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.event.NewEventDto;
import ru.practicum.user.dto.event.UpdateEventUserRequest;
import ru.practicum.user.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.user.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.user.dto.request.ParticipationRequestDto;
import ru.practicum.user.dto.user.NewUserRequest;
import ru.practicum.user.dto.user.UserDto;

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

    //------------------Admin-----------------------

    UserDto addUser(NewUserRequest newUserRequest);

    void deleteUser(long userId);

    List<UserDto> getAllUsers(List<Long> ids, int from, int size);
}