package ru.practicum.user.dto.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class NewUserRequest {
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
    @Email
    @Size(min = 6, max = 254)
    @NotBlank
    private String email;
}