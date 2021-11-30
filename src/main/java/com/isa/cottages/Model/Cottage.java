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
public class Cottage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String residence;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String promotionalDescription;

    @Column
    private String image;

    @Column
    private Long numberOfRooms;

    @Column
    private Long numberOfBeds;

    @Column
    private String rules;

    @Column
    private String additionalServices;

    @Column
    private Boolean reserved;

    @Column
    private Boolean deleted = false;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime availableFrom;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime availableUntil;
    
    @Column
    private Double averageRating = 0.0;

    @ElementCollection
    private Set<Integer> ratings;

    @ManyToOne(targetEntity = CottageOwner.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="cottage_owner_id", nullable=true, referencedColumnName = "id")
    private CottageOwner cottageOwner;

    @OneToMany(mappedBy = "cottage", targetEntity = CottageReservation.class)
    private Set<CottageReservation> cottageReservations = new HashSet<>();
}