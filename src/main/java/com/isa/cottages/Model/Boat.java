package com.isa.cottages.Model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Boat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String boatName;

    @Column
    private EngineType engineType;

    @Column
    private Long length;

    @Column
    private Long engineNumber;

    @Column
    private Long enginePower;

    @Column
    private Long maxSpeed;

    @Column
    private String residence;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String imageUrl;

    @Column
    private Long capacity;

    @Column
    private String rules;

    @Column
    private Double price;

    @Column
    private String description;

    @Column
    private CancellationCondition cancellationCondition;

    @Column
    private Boolean deleted = false;

    @Column
    private Boolean reserved = false;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime availableFrom;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime availableUntil;

    @ManyToOne(targetEntity = BoatOwner.class)
    private BoatOwner boatOwner;

    @Column
    private Double averageRating = 0.0;

    @ElementCollection
    private Set<Integer> ratings;

    @ManyToOne(targetEntity = Client.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "subscriber_id", nullable = true, referencedColumnName = "id")
    private Client subscriber;

    @OneToMany(mappedBy = "boat", targetEntity = AdditionalService.class)
    private Set<AdditionalService> additionalServices = new HashSet<>();

    @OneToMany(mappedBy = "boat", targetEntity = NavigationEquipment.class)
    private Set<NavigationEquipment> navigationEquipments = new HashSet<>();
}
