package ru.practicum.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.admin.dto.user.NewUserRequest;
import ru.practicum.admin.dto.user.UserDto;
import ru.practicum.admin.dto.user.UserShortDto;
import ru.practicum.user.model.User;

@Component
public class UserMapper {
    public User toEntity(NewUserRequest dto) {
        User entity = new User();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        return entity;
    }

    public UserDto toFullDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .name(entity.getName())
                .build();
    }

    public UserShortDto toShortDto(User entity) {
        return UserShortDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
