package ru.practicum.event.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.StatClient;
import ru.practicum.dtos.HitForStatDto;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements StatService {

    private final StatClient statsClient;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void addHit(HttpServletRequest request) {
        statsClient.addHit(
                "main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now());
    }

    @Override
    public List<HitForStatDto> getStats(LocalDateTime start, LocalDateTime end, Boolean unique, List<String> uris) {
        ResponseEntity<Object> response = statsClient.getStatistics(start, end, unique, uris);
        try {
            return Arrays.asList(mapper.readValue(mapper.writeValueAsString(response.getBody()), HitForStatDto[].class));
        } catch (IOException exception) {
            throw new ClassCastException(exception.getMessage());
        }
    }

    @Override
    public Long getViews(Long eventId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<HitForStatDto> hits = getStats(
                LocalDateTime.parse(formatter.format(LocalDateTime.now().minusYears(50)), formatter),
                LocalDateTime.parse(formatter.format(LocalDateTime.now()), formatter),
                true,
                List.of("/events/" + eventId));
        if (!hits.isEmpty()) {
            return hits.get(0).getHits();
        } else {
            return 0L;
        }
    }

}
