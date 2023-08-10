package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.user.NewUserRequest;
import ru.practicum.user.dto.user.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {
    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("Запрос addUser начат с телом {}", newUserRequest);
        UserDto result = service.addUser(newUserRequest);
        log.info("Результат {}", result);
        return result;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long userId) {
        log.info("Запрос deleteUser начат с id {}", userId);
        service.deleteUser(userId);
        log.info("Запрос deleteUser завершен");
    }

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(required = false) List<Long> ids,
                                     @RequestParam(defaultValue = "0") @Min(0) int from,
                                     @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("Запрос getAllUsers начат с параметрами {}, {}, {}", ids, from, size);
        List<UserDto> result = service.getAllUsers(ids, from, size);
        log.info("Запрос getAllUsers завершен {}", result);
        return result;
    }
}
