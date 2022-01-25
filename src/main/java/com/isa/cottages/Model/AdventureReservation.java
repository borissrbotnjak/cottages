package com.isa.cottages.Model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;

@Entity
@DiscriminatorValue("adventure_reservation")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdventureReservation extends Reservation {
    @ManyToOne(targetEntity = Instructor.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "instructor_id", nullable = true, referencedColumnName = "id")
    private Instructor instructor;

    @ManyToOne(targetEntity = FishingInstructorAdventure.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "adventure_id", nullable = true, referencedColumnName = "id")
    private FishingInstructorAdventure adventure;



}
