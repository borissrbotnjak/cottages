package com.isa.cottages.Model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String text;

    @Column
    private Boolean penal;

    @Column
    private Boolean didAppear;

    @ManyToOne(targetEntity = CottageOwner.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cottage_owner_id")
    private CottageOwner cottageOwner;

    @ManyToOne(targetEntity = Instructor.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;
}
