package ru.practicum.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class HitForStatDto {
    @NotBlank
    private String app;

    @NotBlank
    private String uri;
    private Long hits;

}