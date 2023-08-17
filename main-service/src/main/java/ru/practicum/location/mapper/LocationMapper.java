package ru.practicum.location.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.dto.NewLocationDto;
import ru.practicum.location.model.Location;

@Component
@RequiredArgsConstructor
public class LocationMapper {
    public Location toEntity(NewLocationDto dto) {
        Location entity = new Location();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setLon(dto.getLon());
        entity.setLat(dto.getLat());
        entity.setCountry(dto.getCountry());
        entity.setType(dto.getType());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    public Location toEntity(LocationDto dto) {
        Location entity = new Location();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setLon(dto.getLon());
        entity.setLat(dto.getLat());
        entity.setCountry(dto.getCountry());
        entity.setType(dto.getType());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    public LocationDto toDto(Location entity) {
        return LocationDto.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .lon(entity.getLon())
                .lat(entity.getLat())
                .country(entity.getCountry())
                .name(entity.getName())
                .status(entity.getStatus())
                .type(entity.getType())
                .build();
    }
}
