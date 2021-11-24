package com.isa.cottages.Model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Boat_owner")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoatOwner extends User {

    @Column
    private String explanationOfRegistration;

    @Column
    private UserRole userRole = UserRole.BOAT_OWNER;

    @Column
    private RegistrationType registrationType = RegistrationType.BOAT_ADVERTISER;

    @OneToMany(mappedBy = "boatOwner", cascade = CascadeType.ALL)
    private Set<Boat> boats = new HashSet<>();
}
