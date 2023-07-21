package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dtos.HitDto;
import ru.practicum.dtos.HitForStatDto;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class HitMapper {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Hit toEntity(HitDto hitDto){
        Hit hit = new Hit();
        hit.setIp(hitDto.getIp());
        hit.setUri(hitDto.getUri());
        hit.setApp(hitDto.getApp());
        hit.setTimestamp(LocalDateTime.parse(hitDto.getTimestamp(), formatter));
        return hit;
    }
    public HitDto toDto(Hit hit){
        return HitDto.builder()
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp().toString())
                .app(hit.getApp())
                .uri(hit.getUri())
                .build();
    }
    public HitForStatDto toStatDtoFromEntity(Hit hit, Long hits){
        return HitForStatDto.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .hits(hits)
                .build();
    }
}
