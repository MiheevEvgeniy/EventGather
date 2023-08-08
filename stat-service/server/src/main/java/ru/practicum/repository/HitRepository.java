package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dtos.HitForStatDto;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface HitRepository extends JpaRepository<Hit, Long> {
    @Query("SELECT h FROM Hit AS h WHERE h.timestamp BETWEEN ?1 AND ?2 ")
    List<Hit> getAllStatistics(LocalDateTime start, LocalDateTime end);

    @Query("SELECT h FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 AND h.uri IN (?3)")
    List<Hit> getAllStatisticsWithUri(LocalDateTime start, LocalDateTime end, List<String> uri);
    @Query("SELECT COUNT(h) FROM Hit AS h " +
            "WHERE h.uri = ?1")
    Long getHitsCount(String uri);
    @Query("SELECT COUNT(DISTINCT (h.ip)) FROM Hit AS h " +
            "WHERE h.uri = ?1 AND h.ip = ?2")
    Long getHitsCountByIp(String uri, String ip);
}
