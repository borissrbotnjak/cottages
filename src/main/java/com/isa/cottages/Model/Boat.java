package com.isa.cottages.Model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Boat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String boatName;

    @Column
    private Long length;

    @Column
    private Long engineNumber;

    @Column
    private Long enginePower;

    @Column
    private Long maxSpeed;

    @Column
    private String residence;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String imageUrl;

    @Column
    private Long capacity;

    @Column
    private String rules;

    @Column
    private String description;

    @ManyToOne(targetEntity = BoatOwner.class)
    private BoatOwner boatOwner;

    @Column
    private Double averageRating = 0.0;

    @ElementCollection
    private Set<Integer> ratings;
}
