package ru.practicum.location.service;

import ru.practicum.enums.LocationStatuses;
import ru.practicum.enums.LocationTypes;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.dto.NewLocationDto;
import ru.practicum.location.dto.UpdateLocationDto;

import java.util.List;

public interface LocationService {
    LocationDto addLocation(NewLocationDto dto);

    LocationDto updateLocation(UpdateLocationDto upd, Long id);

    LocationDto getLocationById(Long id);

    List<LocationDto> findAllLocations(String name,
                                       String country,
                                       LocationStatuses status,
                                       LocationTypes type,
                                       int from,
                                       int size);

    void deleteLocationById(Long id);
}