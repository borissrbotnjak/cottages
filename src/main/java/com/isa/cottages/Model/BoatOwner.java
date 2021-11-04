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

    @OneToMany(mappedBy = "boatOwner", cascade = CascadeType.ALL)
    private Set<Boat> boats = new HashSet<>();

    @Column
    private RegistrationType registrationType;

    @Column
    private String explanationOfRegistration;
}
