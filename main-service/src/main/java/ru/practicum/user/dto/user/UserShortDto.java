package ru.practicum.user.dto.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserShortDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
}