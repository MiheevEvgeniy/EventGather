package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.enums.States;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminDto;
import ru.practicum.event.service.EventService;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {
    private final EventService service;

    @GetMapping
    public List<EventFullDto> searchEvent(@RequestParam(required = false) List<Long> users,
                                          @RequestParam(required = false) List<States> states,
                                          @RequestParam(required = false) List<Long> categories,
                                          @RequestParam(required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME,
                                                  pattern = "yyyy-MM-dd HH:mm:ss")
                                          LocalDateTime rangeStart,
                                          @RequestParam(required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME,
                                                  pattern = "yyyy-MM-dd HH:mm:ss")
                                          LocalDateTime rangeEnd,
                                          @RequestParam(defaultValue = "0") @Min(0) int from,
                                          @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("Запрос searchEvent начат с параметрами:\n" +
                "{};\n" +
                "{};\n" +
                "{};\n" +
                "{};\n" +
                "{};\n" +
                "{};\n" +
                "{}.\n", users, states, categories, rangeStart, rangeEnd, from, size);
        List<EventFullDto> result = service.searchEvent(
                users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                from,
                size);
        log.info("Результат {}", result);
        return result;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable long eventId,
                                    @RequestBody UpdateEventAdminDto upd) {
        log.info("Запрос updateCategory начат с телом {} и id {}", upd, eventId);
        EventFullDto result = service.updateEvent(eventId, upd);
        log.info("Результат {}", result);
        return result;
    }
}
