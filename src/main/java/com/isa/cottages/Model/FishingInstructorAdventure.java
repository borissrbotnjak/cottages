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
    @NonNull
    private String adventureResidence;

    @Column
    @NonNull
    private String adventureCity;

    @Column
    @NonNull
    private String adventureState;

    @Column
    private String adventureDescription;

    @Column
    @NonNull
    private String maxClients;

    @Column
    private String quickReservation = "No";

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
    @NonNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime availableFrom;

    @Column
    @NonNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime availableUntil;

    @Column
    private String gearIncluded;

    @Column
    @NonNull
    private Double price;

    @Column
    private Double cancellationFeePercent;

    @Column
    private String instructorInfo;

    @ManyToOne(targetEntity = Instructor.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "instructor_id", nullable = true, referencedColumnName = "id")
    private Instructor instructor;

    @ManyToOne(targetEntity = Client.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "subscriber_id", nullable = true, referencedColumnName = "id")
    private Client subscriber;
    @OneToMany(mappedBy = "adventure", targetEntity = AdventureReservation.class)
    private Set<AdventureReservation> adventureReservations = new HashSet<>();

    @OneToMany(mappedBy = "adventure", targetEntity = AdditionalService.class)
    @NonNull
    private Set<AdditionalService> additionalServices = new HashSet<>();

    public FishingInstructorAdventure(String images) {
        this.imageUrl = imageUrl;
    }
}
