package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dtos.HitForStatDto;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {
    @Query("SELECT h FROM Hit h WHERE h.timestamp > ?1 AND h.timestamp < ?2")
    List<Hit> getStatistic(LocalDateTime start, LocalDateTime end);
    @Query("SELECT h FROM Hit h WHERE h.timestamp > ?1 AND h.timestamp < ?2 AND h.uri IN ?3")
    List<Hit> getStatisticWithUris(LocalDateTime start, LocalDateTime end, List<String> uris);
    Long countByUri(String uri);
    @Query("SELECT COUNT(DISTINCT(h.ip)) FROM Hit h WHERE h.uri = ?1")
    Long countByDistinctIp(String uri);
}
