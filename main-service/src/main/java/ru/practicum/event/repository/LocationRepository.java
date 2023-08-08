package ru.practicum.event.repository;

import org.hibernate.annotations.OrderBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.enums.States;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventLocation;

import java.time.LocalDateTime;
import java.util.List;

public interface LocationRepository extends JpaRepository<EventLocation, Long> {
}