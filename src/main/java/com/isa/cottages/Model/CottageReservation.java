package com.isa.cottages.Model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CottageReservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @Column
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startingTime;

    @Column
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;

    @Column
    private Boolean discount = false;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime actionAvailableFrom;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime actionAvailableUntil;

    @Column
    private Long maxPersons;

    @Column
    private String additionalServices;

    @Column
    private Double price;

    @Column
    private Double duration;

    @Column
    private Boolean reserved = false;

    @Column
    private Boolean deleted = false;

    @ManyToOne(targetEntity = CottageOwner.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cottage_owner_id", nullable = true, referencedColumnName = "id")
    private CottageOwner cottageOwner;

    @ManyToOne(targetEntity = Cottage.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cottage_id", nullable = true, referencedColumnName = "id")
    private Cottage cottage;

    @ManyToOne(targetEntity = Client.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", nullable = true, referencedColumnName = "id")
    private Client client;

    @ManyToOne(targetEntity = BoatOwner.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "boat_owner_id", nullable = true, referencedColumnName = "id")
    private BoatOwner boatOwner;

    @ManyToOne(targetEntity = Boat.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "boat_id", nullable = true, referencedColumnName = "id")
    private Boat boat;
}
