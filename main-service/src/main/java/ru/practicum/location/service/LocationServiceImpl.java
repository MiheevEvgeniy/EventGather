package ru.practicum.location.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import ru.practicum.enums.LocationStatuses;
import ru.practicum.enums.LocationTypes;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.dto.NewLocationDto;
import ru.practicum.location.dto.UpdateLocationDto;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.DynamicLocationRepository;
import ru.practicum.location.repository.LocationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository repository;
    private final DynamicLocationRepository dynamicLocationRepository;
    private final LocationMapper mapper;

    @Override
    public LocationDto addLocation(NewLocationDto dto) {
        Location foundedLocation = repository.findByLatAndLon(dto.getLat(), dto.getLon());
        if (foundedLocation != null) {
            throw new ConflictException("This coordinates are unavailable");
        }
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public LocationDto updateLocation(UpdateLocationDto upd, Long id) {
        Location actualLocation = repository.getById(id);
        if (upd.getCountry() != null) {
            actualLocation.setCountry(upd.getCountry());
        }
        if (upd.getName() != null) {
            actualLocation.setName(upd.getName());
        }
        if (upd.getLon() != null) {
            actualLocation.setLon(upd.getLon());
        }
        if (upd.getLat() != null) {
            actualLocation.setLat(upd.getLat());
        }
        if (upd.getStatus() != null) {
            actualLocation.setStatus(upd.getStatus());
        }
        if (upd.getType() != null) {
            actualLocation.setType(upd.getType());
        }
        if (upd.getDescription() != null) {
            actualLocation.setDescription(upd.getDescription());
        }
        return mapper.toDto(repository.save(actualLocation));

    }

    @Override
    public LocationDto getLocationById(Long id) {
        return mapper.toDto(repository.getById(id));
    }

    @Override
    public List<LocationDto> findAllLocations(String name,
                                              String country,
                                              LocationStatuses status,
                                              LocationTypes type,
                                              int from,
                                              int size) {
        PagedListHolder<LocationDto> page = new PagedListHolder<>(dynamicLocationRepository.search(name, country, status, type)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList()));
        page.setPageSize(size);
        page.setPage(from);
        return new ArrayList<>(page.getPageList());
    }

    @Override
    public void deleteLocationById(Long id) {
        Optional<Location> foundedLocation = repository.findById(id);
        if (foundedLocation.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new NotFoundException("Location not found");
        }
    }
}
