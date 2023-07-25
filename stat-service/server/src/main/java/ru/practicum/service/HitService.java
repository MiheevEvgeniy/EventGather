package ru.practicum.service;

import ru.practicum.dtos.HitDto;
import ru.practicum.dtos.HitForStatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    List<HitForStatDto> getStatistics(LocalDateTime start,
                                      LocalDateTime end,
                                      Boolean unique,
                                      List<String> uris);


    String addHit(HitDto hitDto);
}
