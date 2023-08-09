package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.repository.CompilationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final CompilationMapper mapper;


    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        PagedListHolder page;
        if (pinned) {
            page = new PagedListHolder(repository.findAllByPinned(pinned)
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList()));
        } else {
            page = new PagedListHolder(repository.findAll()
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
}
