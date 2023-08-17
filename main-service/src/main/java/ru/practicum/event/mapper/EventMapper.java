package ru.practicum.event.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.user.dto.event.NewEventDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final LocationMapper locationMapper;

    public Event toEntity(NewEventDto dto, Category category, User initiator) {
        Event entity = new Event();
        entity.setTitle(dto.getTitle());
        entity.setInitiator(initiator);
        entity.setAnnotation(dto.getAnnotation());
        entity.setCategory(category);
        entity.setDescription(dto.getDescription());
        entity.setParticipantLimit(dto.getParticipantLimit());
        entity.setEventDate(dto.getEventDate());
        entity.setLocation(locationMapper.toEntity(dto.getLocation()));
        entity.setCreatedOn(LocalDateTime.now());
        entity.setPaid(dto.isPaid());
        entity.setRequestModeration(dto.getRequestModeration());
        return entity;
    }

    public EventFullDto toFullDto(Event entity, int confirmedRequestsCount) {
        return EventFullDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .annotation(entity.getAnnotation())
                .category(categoryMapper.toDto(entity.getCategory()))
                .description(entity.getDescription())
                .views(entity.getViews())
                .eventDate(entity.getEventDate())
                .participantLimit(entity.getParticipantLimit())
                .confirmedRequests(confirmedRequestsCount)
                .createdOn(entity.getCreatedOn())
                .initiator(userMapper.toShortDto(entity.getInitiator()))
                .location(locationMapper.toDto(entity.getLocation()))
                .state(entity.getState())
                .paid(entity.isPaid())
                .requestModeration(entity.isRequestModeration())
                .build();
    }

    public EventShortDto toShortDto(Event entity) {
        return EventShortDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .views(entity.getViews())
                .annotation(entity.getAnnotation())
                .category(categoryMapper.toDto(entity.getCategory()))
                .eventDate(entity.getEventDate())
                .initiator(userMapper.toShortDto(entity.getInitiator()))
                .paid(entity.isPaid())
                .build();
    }
}
