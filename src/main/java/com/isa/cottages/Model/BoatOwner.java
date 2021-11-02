package com.isa.cottages.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Ship_owner")
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
