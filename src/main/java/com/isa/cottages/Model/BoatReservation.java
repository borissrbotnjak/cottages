package com.isa.cottages.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@DiscriminatorValue("boat_reservation")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoatReservation extends Reservation{

    @ManyToOne(targetEntity = BoatOwner.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "boat_owner_id", nullable = true, referencedColumnName = "id")
    private BoatOwner boatOwner;

    @ManyToOne(targetEntity = Boat.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "boat_id", nullable = true, referencedColumnName = "id")
    private Boat boat;

}
