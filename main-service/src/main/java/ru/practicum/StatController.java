package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dtos.HitDto;

import java.util.List;

@RestController
@RequestMapping
@Slf4j
public class StatController {

    private final StatClient client;

    @Autowired
    public StatController(@Value("${ewm-stat-server.url}") String serverUrl,
                          RestTemplateBuilder builder) {
        this.client = new StatClient(serverUrl, builder);
    }

    @PostMapping("hit")
    public ResponseEntity<Object> addHit(@RequestBody HitDto hitDto) {
        return client.addHit(hitDto);
    }

    @GetMapping("stats")
    public ResponseEntity<Object> getStatistics(@RequestParam
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME,
                                                        pattern = "yyyy-MM-dd HH:mm:ss")
                                                String start,
                                                @RequestParam
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME,
                                                        pattern = "yyyy-MM-dd HH:mm:ss")
                                                String end,
                                                @RequestParam(defaultValue = "false") Boolean unique,
                                                @RequestParam(required = false) List<String> uris) {
        return client.getStatistics(start, end, unique, uris);
    }
}
