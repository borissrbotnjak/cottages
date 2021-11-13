package com.isa.cottages.Model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("Client")
public class Client extends User {

    @Column
    private Double loyaltyPoints;

    @Column
    private Integer discount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "loyalty_program_id", referencedColumnName = "id")
    private LoyaltyProgram  loyaltyProgram;
}
