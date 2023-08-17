package ru.practicum.location.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.enums.LocationStatuses;
import ru.practicum.enums.LocationTypes;

import javax.validation.constraints.*;

@Data
@Builder
public class NewLocationDto {
    @NotBlank
    private String name;
    @Size(min = 100, max = 3000)
    @NotBlank
    private String description;
    @NotBlank
    private String country;
    @NotNull
    private LocationTypes type;
    @Min(-90)
    @Max(90)
    private double lat; //Широта
    @Min(-180)
    @Max(180)
    private double lon; //Долгота
    @NotNull
    private LocationStatuses status;
}