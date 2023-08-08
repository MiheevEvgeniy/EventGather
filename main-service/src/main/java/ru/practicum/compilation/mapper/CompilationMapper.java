package ru.practicum.compilation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.dtos.HitDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapper eventMapper;
    public Compilation toEntity(NewCompilationDto dto, List<Event> events) {
        Compilation entity = new Compilation();
        entity.setEvents(events);
        entity.setPinned(dto.isPinned());
        entity.setTitle(dto.getTitle());
        return entity;
    }
    public Compilation toEntity(UpdateCompilationRequest dto, List<Event> events) {
        Compilation entity = new Compilation();
        entity.setEvents(events);
        entity.setPinned(dto.isPinned());
        entity.setTitle(dto.getTitle());
        return entity;
    }

    public CompilationDto toDto(Compilation entity) {
        return CompilationDto.builder()
                .id(entity.getId())
                .events(entity.getEvents()
                        .stream()
                        .map(eventMapper::toShortDto)
                        .collect(Collectors.toList()))
                .title(entity.getTitle())
                .pinned(entity.isPinned())
                .build();
    }
}
