package com.isa.cottages.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@DiscriminatorValue("additional_service")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalService implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private Double price;

    @Column
    private String name;

    @ManyToOne(targetEntity = FishingInstructorAdventure.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "adventure_id", nullable = true, referencedColumnName = "id")
    private FishingInstructorAdventure adventure;
}
