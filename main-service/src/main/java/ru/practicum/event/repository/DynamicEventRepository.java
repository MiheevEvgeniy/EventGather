package ru.practicum.event.repository;

import org.hibernate.annotations.OrderBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.enums.EventSortBy;
import ru.practicum.enums.States;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface DynamicEventRepository {
    List<Event> searchEvent(List<Long> users,
                            List<States> states,
                            List<Long> categories,
                            LocalDateTime rangeStart,
                            LocalDateTime rangeEnd);
    List<Event> getEvents(String text,
                                  List<Long> categories,
                                  boolean paid,
                                  LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd,
                                  boolean onlyAvailable,
                                  String sort);
}