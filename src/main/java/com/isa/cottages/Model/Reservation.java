package com.isa.cottages.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "reservation")
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "reservation_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    @Column
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;

    @Column
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @Column
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;

    @Column
    private String startDateString;

    @Column
    private String endDateString;

    @Column
    private Boolean discount = false;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime discountAvailableFrom;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime discountAvailableUntil;

    @Column
    private Integer numPersons;

    @Column
    private Double price;

    @Column
    private Double discountPrice = 0.0;

    @Column
    private Double duration;

    @Column
    private Boolean reserved = false;

    @Column
    private Boolean deleted = false;

    @ManyToOne(targetEntity = Client.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", nullable = true, referencedColumnName = "id")
    private Client client;

    @OneToMany(targetEntity = AdditionalService.class, mappedBy = "reservation")
    private Set<AdditionalService> additionalServices = new HashSet<>();


    public void CalculatePrice() {
        Double sum = price;
        if (this.discount && this.discountPrice != 0.0) {
            sum = discountPrice;
        }

        for (AdditionalService s : this.additionalServices) {
            sum += s.getPrice();
        }

        this.price = sum;
    }

}
