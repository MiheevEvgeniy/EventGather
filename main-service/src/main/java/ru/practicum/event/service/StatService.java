package ru.practicum.event.service;

import ru.practicum.dtos.HitForStatDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void addHit(HttpServletRequest request);

    List<HitForStatDto> getStats(LocalDateTime start, LocalDateTime end, Boolean unique, List<String> uris);

    Long getViews(Long eventId);
}
