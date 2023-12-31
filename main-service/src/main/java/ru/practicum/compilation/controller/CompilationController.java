package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationController {
    private final CompilationService service;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                @RequestParam(defaultValue = "0")
                                                @Min(0)
                                                Integer from,
                                                @RequestParam(defaultValue = "10")
                                                @Min(1)
                                                Integer size) {
        log.info("Запрос getCompilations начат с параметрами: " +
                "pinned = {}; " +
                "from = {}; " +
                "size = {}.", pinned, from, size);
        return service.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable long compId) {
        log.info("Запрос getCompilationById начат с id: {}", compId);
        return service.getCompilationById(compId);
    }
}
