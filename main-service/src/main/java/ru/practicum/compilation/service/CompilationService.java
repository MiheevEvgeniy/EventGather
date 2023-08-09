package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilations(Boolean pinned,
                                         Integer from,
                                         Integer size);

    CompilationDto getCompilationById(long compId);
}