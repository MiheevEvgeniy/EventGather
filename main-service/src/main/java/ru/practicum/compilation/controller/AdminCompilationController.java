package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {
    private final CompilationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Запрос addCompilation начат с телом {}", newCompilationDto);
        CompilationDto result = service.addCompilation(newCompilationDto);
        log.info("Результат {}", result);
        return result;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable long compId) {
        log.info("Запрос deleteCompilation начат с id {}", compId);
        service.deleteCompilation(compId);
        log.info("Запрос deleteCompilation завершен");
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@RequestBody UpdateCompilationRequest updComp,
                                            @PathVariable long compId) {
        log.info("Запрос updateCompilation начат с телом {} и id {}", updComp, compId);
        CompilationDto result = service.updateCompilation(updComp, compId);
        log.info("Результат {}", result);
        return result;
    }
}
