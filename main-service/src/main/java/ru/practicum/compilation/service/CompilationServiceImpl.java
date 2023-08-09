package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.BadRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final CompilationMapper mapper;

    private final EventRepository eventRepository;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        PagedListHolder<CompilationDto> page;
        if (pinned) {
            page = new PagedListHolder<>(repository.findAllByPinned(pinned)
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList()));
        } else {
            page = new PagedListHolder<>(repository.findAll()
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList()));
        }
        page.setPageSize(size);
        page.setPage(from);
        return new ArrayList<>(page.getPageList());
    }

    @Override
    public CompilationDto getCompilationById(long compId) {
        return mapper.toDto(repository.getById(compId));
    }

    //---------------------Admin-------------------------

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = new ArrayList<>();
        if (newCompilationDto.getEvents() != null) {
            for (Long id : newCompilationDto.getEvents()) {
                events.add(eventRepository.getById(id));
            }
        }
        return mapper.toDto(
                repository.save(
                        mapper.toEntity(newCompilationDto, events)));
    }

    @Override
    public void deleteCompilation(long compId) {
        repository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(UpdateCompilationRequest updComp, long compId) {
        Compilation actualCompilation = repository.getById(compId);
        List<Event> events = new ArrayList<>();
        if (updComp.getEvents() != null) {
            for (Long id : updComp.getEvents()) {
                events.add(eventRepository.getById(id));
            }
        }
        Compilation updCompilation = mapper.toEntity(updComp, events);
        if (updCompilation.getEvents() != null) {
            actualCompilation.setEvents(updCompilation.getEvents());
        }
        if (updCompilation.isPinned() != actualCompilation.isPinned()) {
            actualCompilation.setPinned(updCompilation.isPinned());
        }
        if (updCompilation.getTitle() != null) {
            if (updCompilation.getTitle().length() > 50) {
                throw new BadRequestException("Compilation title is invalid");
            }
            actualCompilation.setTitle(updCompilation.getTitle());
        }
        return mapper.toDto(repository.save(actualCompilation));
    }
}
