package ru.practicum.user.model;

import lombok.*;
import ru.practicum.enums.States;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Requests", schema = "public")
@Getter
@Setter
@ToString
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, name = "created")
    private LocalDateTime createdOn;
    @Column(nullable = false, name = "event_id")
    private long event;
    @Column(nullable = false, name = "requester_id")
    private long requester;
    @Enumerated(EnumType.STRING)
    private States status;
}