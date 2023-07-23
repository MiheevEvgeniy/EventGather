package ru.practicum.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class HitDto {

    @NotBlank
    private String app;

    @NotBlank
    private String uri;
    @NotBlank
    @Pattern(regexp = "^[0-9]{1,3}\\\\.[0-9]{1,3}\\\\.[0-9]{1,3}\\\\.[0-9]{1,3}$")
    private String ip;
    private String timestamp;

}