package com.isa.cottages.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@DiscriminatorValue("review")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String reviewText;

    @Column
    private Boolean Approved = false;

    @Column
    private Integer rating;

    @ManyToOne(targetEntity = Client.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "clientId")
    private Client client;

    @ManyToOne(targetEntity = Boat.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "boat_id")
    private Boat boat;

    @ManyToOne(targetEntity = Cottage.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cottage_id")
    private Cottage cottage;

    @ManyToOne(targetEntity = FishingInstructorAdventure.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "adventure_id")
    private FishingInstructorAdventure adventure;
}
