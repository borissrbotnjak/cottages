package com.isa.cottages.Model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
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
    private String residence;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String description;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime availableFrom;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime availableUntil;

    @Column
    private Double price = 0.0;

    @Column
    private Integer numPersons;

    @ManyToOne(targetEntity = BoatOwner.class)
    private BoatOwner boatOwner;

    @Column
    private Double averageRating = 0.0;

    @ElementCollection
    private Set<Integer> ratings;

    @ManyToMany(mappedBy = "boatSubscriptions")
    private Set<Client> subscribers;

    @OneToMany(mappedBy = "boat", targetEntity = AdditionalService.class)
    private Set<AdditionalService> additionalServices = new HashSet<>();
}
