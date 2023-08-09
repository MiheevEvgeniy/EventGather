package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.EventLocation;

public interface LocationRepository extends JpaRepository<EventLocation, Long> {
}