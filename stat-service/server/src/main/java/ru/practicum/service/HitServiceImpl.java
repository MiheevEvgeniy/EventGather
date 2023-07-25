package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dtos.HitDto;
import ru.practicum.dtos.HitForStatDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.Hit;
import ru.practicum.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        Set<String> set = new HashSet<>();
        List<Hit> hits;
        if (uris != null) {
            hits = repository.getStatisticWithUris(start, end, uris);
        } else {
            hits = repository.getStatistic(start, end);
        }
        if (!unique) {
            return hits.stream()
                    .filter(hit -> set.add(hit.getUri()))
                    .map(hit -> mapper.toStatDtoFromEntity(hit, repository.countByUri(hit.getUri())))
                    .sorted(Comparator.comparingLong(HitForStatDto::getHits).reversed())
                    .collect(Collectors.toList());
        } else {
            return hits.stream()
                    .filter(hit -> set.add(hit.getIp()))
                    .filter(hit -> set.add(hit.getUri()))
                    .map(hit -> mapper.toStatDtoFromEntity(hit, repository.countByDistinctIp(hit.getUri())))
                    .sorted(Comparator.comparingLong(HitForStatDto::getHits).reversed())
                    .collect(Collectors.toList());
        }

    }

    @Override
    public String addHit(HitDto hitDto) {
        repository.save(mapper.toEntity(hitDto));
        return "Информация сохранена";
    }
}
