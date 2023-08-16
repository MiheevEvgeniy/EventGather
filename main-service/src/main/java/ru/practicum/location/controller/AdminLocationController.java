package ru.practicum.location.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.enums.LocationStatuses;
import ru.practicum.enums.LocationTypes;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.dto.NewLocationDto;
import ru.practicum.location.dto.UpdateLocationDto;
import ru.practicum.location.service.LocationService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/admin/locations")
@RequiredArgsConstructor
@Slf4j
public class AdminLocationController {
    private final LocationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDto addLocation(@Valid @RequestBody NewLocationDto dto) {
        log.info("Запрос addLocation начат с телом: {}", dto);
        LocationDto result = service.addLocation(dto);
        log.info("Результат запроса: {}", result);
        return result;
    }

    @PatchMapping("/{id}")
    public LocationDto updateLocation(@RequestBody UpdateLocationDto upd, @PathVariable Long id) {
        log.info("Запрос updateLocation начат с телом {} и id {}", upd, id);
        LocationDto result = service.updateLocation(upd, id);
        log.info("Результат запроса: {}", result);
        return result;
    }

    @GetMapping("/{id}")
    public LocationDto getLocationById(@PathVariable Long id) {
        log.info("Запрос getLocationById начат с и id {}", id);
        LocationDto result = service.getLocationById(id);
        log.info("Результат запроса: {}", result);
        return result;
    }

    @GetMapping("/search")
    public List<LocationDto> findAllLocations(@RequestParam(required = false) String name,
                                              @RequestParam(required = false) String country,
                                              @RequestParam(required = false) LocationStatuses status,
                                              @RequestParam(required = false) LocationTypes type,
                                              @RequestParam(defaultValue = "0") @Min(0) int from,
                                              @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("Запрос findAllLocations с параметрами: " +
                "name: {}; " +
                "country: {}; " +
                "status: {}; " +
                "from: {}; " +
                "size: {}; " +
                "type: {}.", name, country, status, from, size, type);
        List<LocationDto> result = service.findAllLocations(name, country, status, type, from, size);
        log.info("Результат запроса: {}", result);
        return result;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocationById(@PathVariable Long id) {
        log.info("Запрос deleteLocationById начат с id: {}", id);
        service.deleteLocationById(id);
        log.info("Удаление локации завершено");
    }
}
