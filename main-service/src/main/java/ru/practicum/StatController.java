package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dtos.HitDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class StatController {

    private final StatClient statClient;
    @Autowired
    public StatController(@Value("${ewm-stat-server.url}") String serverUrl, RestTemplateBuilder builder) {

        this.statClient = new StatClient(builder
               .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
               .requestFactory(HttpComponentsClientHttpRequestFactory::new).build());
    }

    @PostMapping("hit")
    public ResponseEntity<Object> addHit(@RequestBody HitDto hitDto) {
        return statClient.post("hit",hitDto);
    }

    @GetMapping("stats")
    public ResponseEntity<Object> getStatistics(@RequestParam
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                LocalDateTime start,
                                             @RequestParam
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                LocalDateTime end,
                                             @RequestParam(defaultValue = "false") Boolean unique,
                                             @RequestParam(required = false) List<String> uris) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "unique",unique,
                "uris",uris
        );
        return statClient.get("stats",parameters);
    }
}
