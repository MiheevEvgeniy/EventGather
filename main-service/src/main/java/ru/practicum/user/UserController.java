package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.event.NewEventDto;
import ru.practicum.user.dto.event.UpdateEventUserRequest;
import ru.practicum.user.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.user.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.user.dto.request.ParticipationRequestDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    //-------------Event-Requests--------------------

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getUsersRequestsInfo(@PathVariable long userId){
        log.info("Запрос getEventRequestInfo начат с id {}", userId);
        List<ParticipationRequestDto> result = service.getUsersRequestsInfo(userId);
        log.info("Результат запроса {}", result);
        return result;
    }
    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto sendEventRequest(@PathVariable long userId,
                                   @RequestParam long eventId){
        log.info("Запрос sendEventRequest начат с userId {} и eventId {}", userId,eventId);
        ParticipationRequestDto result = service.sendEventRequest(userId, eventId);
        log.info("Результат запроса {}", result);
        return result;
    }
    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelEventRequest(@PathVariable long userId,
                                     @PathVariable long requestId){
        log.info("Запрос cancelEventRequest начат с userId {} и requestId {}", userId,requestId);
        ParticipationRequestDto result = service.cancelEventRequest(userId, requestId);
        log.info("Результат запроса {}", result);
        return result;
    }
    //---------------Events-------------------------
    @GetMapping("/{userId}/events")
    public List<EventShortDto> getOwnedEvents(@PathVariable long userId,
                                              @RequestParam(defaultValue = "0") @Min(0) int from,
                                              @RequestParam(defaultValue = "10") @Min(1) int size){
        log.info("Выполнение запроса getOwnedEvents начато с id {}, from {}, size {}", userId, from,size);
        List<EventShortDto> result =service.getOwnedEvents(userId,from,size);
        log.info("Результат запроса {}", result);
        return result;
    }
    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable long userId,
                                 @Valid @RequestBody NewEventDto newEventDto){
        log.info("Выполнение запроса addEvent начато с id {} и телом {}", userId, newEventDto);
        EventFullDto result =service.addEvent(userId,newEventDto);
        log.info("Результат запроса {}", result);
        return result;
    }
    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventInfo(@PathVariable long userId,
                               @PathVariable long eventId){
        log.info("Выполнение запроса getEventInfo начато с userId {} и eventId {}", userId, eventId);
        EventFullDto result =service.getEventInfo(userId,eventId);
        log.info("Результат запроса {}", result);
        return result;
    }
    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateOwnedEvent(@PathVariable long userId,
                                   @PathVariable long eventId,
                                   @Valid
                                   @RequestBody UpdateEventUserRequest updBody){
        log.info("Выполнение запроса updateOwnedEvent начато с userId {} и eventId {} и телом {}"
                , userId, eventId, updBody);
        EventFullDto result = service.updateOwnedEvent(userId,eventId,updBody);
        log.info("Результат запроса {}", result);
        return result;
    }
    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequestsInfo(@PathVariable long userId,
                                                        @PathVariable long eventId){
        log.info("Выполнение запроса getEventRequestsInfo начато с userId {} и eventId {}", userId, eventId);
        List<ParticipationRequestDto> result =service.getEventRequestsInfo(userId,eventId);
        log.info("Результат запроса {}", result);
        return result;
    }
    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequestsStatus(@PathVariable long userId,
                                                                    @PathVariable long eventId,
                                                                    @RequestBody EventRequestStatusUpdateRequest updRequest){
        log.info("Выполнение запроса updateEventRequestsStatus начато с userId {} и eventId {} и телом {}"
                , userId, eventId, updRequest);
        EventRequestStatusUpdateResult result =service.updateEventRequestsStatus(userId,eventId, updRequest);
        log.info("Результат запроса {}", result);
        return result;
    }
}
