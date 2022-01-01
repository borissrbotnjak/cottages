package com.isa.cottages.Model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("Client")
public class Client extends User {

    @Column
    private Double loyaltyPoints;

    @Column
    private Integer discount;

    @ElementCollection
    private Set<Integer> penals;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "loyalty_program_id", referencedColumnName = "id")
    private LoyaltyProgram  loyaltyProgram;

    @OneToMany(mappedBy = "client", targetEntity = Reservation.class)
    private Set<Reservation> Reservations = new HashSet<>();

    @OneToMany(mappedBy = "subscriber", targetEntity = Boat.class)
    private Set<Boat> boatSubscriptions = new HashSet<>();

    @OneToMany(mappedBy = "subscriber", targetEntity = Cottage.class)
    private Set<Cottage> cottageSubscriptions = new HashSet<>();

    @OneToMany(mappedBy = "subscriber", targetEntity = FishingInstructorAdventure.class)
    private Set<FishingInstructorAdventure> instructorSubscriptions = new HashSet<>();

    @OneToMany(mappedBy = "client", targetEntity = Report.class)
    private Set<Report> reports = new HashSet<>();
}
