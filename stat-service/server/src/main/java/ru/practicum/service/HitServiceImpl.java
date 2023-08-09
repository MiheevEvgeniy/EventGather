package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dtos.HitDto;
import ru.practicum.dtos.HitForStatDto;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.Hit;
import ru.practicum.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {
    private final HitRepository repository;
    private final HitMapper mapper;

    @Override
    public List<HitForStatDto> getStatistics(LocalDateTime start,
                                             LocalDateTime end,
                                             Boolean unique,
                                             List<String> uris) {
        if(start.isAfter(end)){
            throw new BadRequestException("Start date is after end date");
        }
        Set<String> set = new HashSet<>();
        List<Hit> hits;
        if (uris != null && !uris.isEmpty()) {
            if (uris.get(0).startsWith("[") && uris.get(0).endsWith("]")) {
                List<String> urisCut = new ArrayList<>();
                for (String uri : uris) {
                    urisCut.add(uri.substring(1, uri.length() - 1));
                }
                System.out.println(urisCut);
                hits = repository.getAllStatisticsWithUri(start, end, urisCut);
            } else {
                hits = repository.getAllStatisticsWithUri(start, end, uris);
            }
        } else {
            hits = repository.getAllStatistics(start, end);
        }
        if (!unique) {
            return hits.stream()
                    .filter(hit -> set.add(hit.getUri()))
                    .map(hit -> mapper.toStatDtoFromEntity(hit, repository.getHitsCount(hit.getUri())))
                    .sorted(Comparator.comparingLong(HitForStatDto::getHits).reversed())
                    .collect(Collectors.toList());
        } else {
            return hits.stream()
                    .filter(hit -> set.add(hit.getIp()))
                    .filter(hit -> set.add(hit.getUri()))
                    .map(hit -> mapper.toStatDtoFromEntity(hit, repository.getHitsCountByIp(hit.getUri(), hit.getIp())))
                    .sorted(Comparator.comparingLong(HitForStatDto::getHits).reversed())
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void addHit(HitDto hitDto) {
        repository.save(mapper.toEntity(hitDto));
    }
}
