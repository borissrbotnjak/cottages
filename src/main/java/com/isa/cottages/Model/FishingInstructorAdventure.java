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
public class FishingInstructorAdventure implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String adventureName;

    @Column
    private String adventureResidence;

    @Column
    private String adventureCity;

    @Column
    private String adventureState;

    @Column
    private String adventureDescription;

    @Column
    private Integer maxClients;

    @Column
    private String imageUrl;

    @Column
    private String conductRules;

    @Column
    private Double averageRating = 0.0;

    @ElementCollection
    private Set<Integer> ratings;

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
    private String gearIncluded;

    @Column
    private Double price;

    @Column
    private Double cancellationFeePercent;

    @Column
    private String instructorInfo;

    @ManyToOne(targetEntity = Instructor.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "instructor_id", nullable = true, referencedColumnName = "id")
    private Instructor instructor;

    @ManyToMany(mappedBy = "adventureSubscriptions")
    private Set<Client> subscribers;

    @OneToMany(mappedBy = "adventure", targetEntity = AdventureReservation.class)
    private Set<AdventureReservation> adventureReservations = new HashSet<>();

    @OneToMany(mappedBy = "adventure", targetEntity = Review.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "adventure", targetEntity = AdditionalService.class)
    private Set<AdditionalService> additionalServices = new HashSet<>();

    public FishingInstructorAdventure(String images) {
        this.imageUrl = images;
    }
}
