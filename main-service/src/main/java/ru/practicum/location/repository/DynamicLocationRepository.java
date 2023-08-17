package ru.practicum.location.repository;

import ru.practicum.enums.LocationStatuses;
import ru.practicum.enums.LocationTypes;
import ru.practicum.location.model.Location;

import java.util.List;

public interface DynamicLocationRepository {
    List<Location> search(String name,
                          String country,
                          LocationStatuses status,
                          LocationTypes type);
}