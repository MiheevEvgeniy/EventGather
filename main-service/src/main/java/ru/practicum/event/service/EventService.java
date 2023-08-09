package ru.practicum.event.service;

import ru.practicum.enums.EventSortBy;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventShortDto> getEvents(String text,
                                  List<Long> categories,
                                  boolean paid,
                                  LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd,
                                  boolean onlyAvailable,
                                  EventSortBy sort,
                                  int from,
                                  int size,
                                  HttpServletRequest request);

    EventFullDto getEventById(long eventId, HttpServletRequest request);
}