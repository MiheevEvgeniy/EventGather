package ru.practicum.location.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.enums.LocationStatuses;
import ru.practicum.enums.LocationTypes;

@Data
@Builder
public class UpdateLocationDto {
    private String name;
    private String description;
    private String country;
    private LocationTypes type;
    private Double lat; //Широта
    private Double lon; //Долгота
    private LocationStatuses status;
}