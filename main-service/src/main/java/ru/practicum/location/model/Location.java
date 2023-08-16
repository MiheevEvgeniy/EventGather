package ru.practicum.location.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.enums.LocationStatuses;
import ru.practicum.enums.LocationTypes;

import javax.persistence.*;

@Entity
@Table(name = "locations", schema = "public")
@Getter
@Setter
@ToString
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String country;
    @Enumerated(EnumType.STRING)
    private LocationTypes type;
    @Column(nullable = false)
    private double lat; //Широта
    @Column(nullable = false)
    private double lon; //Долгота
    @Enumerated(EnumType.STRING)
    private LocationStatuses status;
}