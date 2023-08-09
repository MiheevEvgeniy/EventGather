package ru.practicum.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.dto.event.UpdateEventAdminDto;
import ru.practicum.admin.dto.user.NewUserRequest;
import ru.practicum.admin.dto.user.UserDto;
import ru.practicum.admin.service.AdminService;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.enums.States;
import ru.practicum.event.dto.EventFullDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService service;

    //-----------Compilations------------------
    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Запрос addCompilation начат с телом {}", newCompilationDto);
        CompilationDto result = service.addCompilation(newCompilationDto);
        log.info("Результат {}", result);
        return result;
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable long compId) {
        log.info("Запрос deleteCompilation начат с id {}", compId);
        service.deleteCompilation(compId);
        log.info("Запрос deleteCompilation завершен");
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@RequestBody UpdateCompilationRequest updComp,
                                            @PathVariable long compId) {
        log.info("Запрос updateCompilation начат с телом {} и id {}", updComp, compId);
        CompilationDto result = service.updateCompilation(updComp, compId);
        log.info("Результат {}", result);
        return result;
    }

    //-----------Categories------------------
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Запрос addCategory начат с телом {}", newCategoryDto);
        CategoryDto result = service.addCategory(newCategoryDto);
        log.info("Результат {}", result);
        return result;
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        log.info("Запрос deleteCategory начат с id {}", catId);
        service.deleteCategory(catId);
        log.info("Запрос deleteCategory завершен");
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable long catId,
                                      @RequestBody CategoryDto body) {
        log.info("Запрос updateCategory начат с телом {} и id {}", body, catId);
        CategoryDto result = service.updateCategory(catId, body);
        log.info("Результат {}", result);
        return result;
    }

    //-------------Events-----------------

    @GetMapping("/events")
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

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable long eventId,
                                    @RequestBody UpdateEventAdminDto upd) {
        log.info("Запрос updateCategory начат с телом {} и id {}", upd, eventId);
        EventFullDto result = service.updateEvent(eventId, upd);
        log.info("Результат {}", result);
        return result;
    }

    //-------------Users--------------------

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("Запрос addUser начат с телом {}", newUserRequest);
        UserDto result = service.addUser(newUserRequest);
        log.info("Результат {}", result);
        return result;
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long userId) {
        log.info("Запрос deleteUser начат с id {}", userId);
        service.deleteUser(userId);
        log.info("Запрос deleteUser завершен");
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers(@RequestParam(required = false) List<Long> ids,
                                     @RequestParam(defaultValue = "0") @Min(0) int from,
                                     @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("Запрос getAllUsers начат с параметрами {}, {}, {}", ids, from, size);
        List<UserDto> result = service.getAllUsers(ids, from, size);
        log.info("Запрос getAllUsers завершен {}", result);
        return result;
    }
}
