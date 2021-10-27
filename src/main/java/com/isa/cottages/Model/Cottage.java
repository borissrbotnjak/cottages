package com.isa.cottages.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Cottage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String residence;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String description;

    @Column
    private Double averageRating = 0.0;

    @ElementCollection
    private Set<Integer> ratings;

}