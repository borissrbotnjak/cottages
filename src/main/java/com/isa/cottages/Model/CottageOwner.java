package com.isa.cottages.Model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Cottage_owner")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CottageOwner extends User {

    @Column
    private String explanationOfRegistration;

    @Column
    private UserRole userRole = UserRole.COTTAGE_OWNER;

    @Column
    private RegistrationType registrationType = RegistrationType.COTTAGE_ADVERTISER;

    @OneToMany(mappedBy = "cottageOwner", targetEntity = CottageReservation.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CottageReservation> cottageReservations = new HashSet<>();

    @OneToMany(mappedBy = "cottageOwner", targetEntity = Cottage.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Cottage> cottages = new HashSet<>();

    @OneToMany(mappedBy = "cottageOwner", targetEntity = Report.class)
    private Set<Report> reports = new HashSet<>();
}
