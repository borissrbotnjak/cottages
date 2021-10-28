package com.isa.cottages.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("ShipOwner")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoatOwner extends User {

    @OneToMany(mappedBy = "boatOwner", cascade = CascadeType.ALL)
    private Set<Boat> boats = new HashSet<>();
}
