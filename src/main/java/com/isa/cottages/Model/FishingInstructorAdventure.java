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
public class FishingInstructorAdventure implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String adventureName;

    @Column
    private String adventureResidence;

    @Column
    private String adventureCity;

    @Column
    private String adventureState;

    @Column
    private String adventureDescription;

    @Column
    private Double averageRating = 0.0;

    @ElementCollection
    private Set<Integer> ratings;

    @Column
    private String instructorInfo;

    @ManyToOne(targetEntity = Instructor.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "instructor_id", nullable = true, referencedColumnName = "id")
    private Instructor instructor;

    @ManyToOne(targetEntity = Client.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "subscriber_id", nullable = true, referencedColumnName = "id")
    private Client subscriber;
}
