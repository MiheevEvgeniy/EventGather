package ru.practicum.admin.service;

import ru.practicum.admin.dto.event.UpdateEventAdminDto;
import ru.practicum.admin.dto.user.NewUserRequest;
import ru.practicum.admin.dto.user.UserDto;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.enums.States;
import ru.practicum.event.dto.EventFullDto;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminService {
    //-----------Compilations------------------
    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(long compId);

    CompilationDto updateCompilation(UpdateCompilationRequest updComp, long compId);

    //-----------Categories------------------
    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(long catId);

    CategoryDto updateCategory(long catId, CategoryDto body);

    //-------------Events-----------------
    List<EventFullDto> searchEvent(List<Long> users,
                                   List<States> states,
                                   List<Long> categories,
                                   LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd,
                                   int from,
                                   int size);

    EventFullDto updateEvent(long eventId, UpdateEventAdminDto upd);

    //-------------Users--------------------
    UserDto addUser(NewUserRequest newUserRequest);

    void deleteUser(long userId);

    List<UserDto> getAllUsers(List<Long> ids, int from, int size);
}