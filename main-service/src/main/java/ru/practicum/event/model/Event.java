package ru.practicum.event.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.category.model.Category;
import ru.practicum.enums.States;
import ru.practicum.user.model.Request;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events", schema = "public")
@Getter
@Setter
@ToString
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(nullable = false, name = "created_on")
    private LocalDateTime createdOn;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false, name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", nullable = false)
    private EventLocation location;
    @Column(nullable = false)
    private boolean paid;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(nullable = false, name = "participant_limit")
    private int participantLimit;
    @ElementCollection
    private List<Request> confirmedRequests;
    @Column(nullable = false, name = "request_moderation")
    private boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private States state;
    @Column(nullable = false)
    private long views;
}