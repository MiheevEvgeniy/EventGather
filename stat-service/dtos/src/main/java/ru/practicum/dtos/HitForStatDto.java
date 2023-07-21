package ru.practicum.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class HitForStatDto {

    @NotBlank
    private String app;

    @NotBlank
    private String uri;
    private Long hits;

}