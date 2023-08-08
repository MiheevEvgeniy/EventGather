package ru.practicum.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.admin.dto.user.NewUserRequest;
import ru.practicum.admin.dto.user.UserDto;
import ru.practicum.admin.dto.user.UserShortDto;
import ru.practicum.user.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.user.dto.request.ParticipationRequestDto;
import ru.practicum.user.model.Request;
import ru.practicum.user.model.User;

import java.util.List;

@Component
public class RequestMapper {
    public Request toEntity(ParticipationRequestDto dto) {
        Request entity = new Request();
        entity.setRequester(dto.getRequester());
        entity.setEvent(dto.getEvent());
        entity.setStatus(dto.getStatus());
        entity.setCreatedOn(dto.getCreated());
        return entity;
    }


    public ParticipationRequestDto toDto(Request entity) {
        return ParticipationRequestDto.builder()
                .id(entity.getId())
                .event(entity.getEvent())
                .created(entity.getCreatedOn())
                .requester(entity.getRequester())
                .status(entity.getStatus())
                .build();
    }
    public EventRequestStatusUpdateResult toResultDto(List<ParticipationRequestDto> confirmed,
                                                      List<ParticipationRequestDto> rejected) {
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmed)
                .rejectedRequests(rejected)
                .build();
    }
}
