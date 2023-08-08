package ru.practicum.compilation.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.compilation.dto.CompilationDto;

import javax.validation.constraints.Min;
import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilations(Boolean pinned,
                                         Integer from,
                                         Integer size);
    CompilationDto getCompilationById(long compId);
}