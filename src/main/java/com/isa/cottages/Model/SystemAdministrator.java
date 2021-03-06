package com.isa.cottages.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("system_administrator")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SystemAdministrator extends User{
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<LoyaltyProgram> loyaltyPrograms = new HashSet<>();

    @Column
    private Boolean isFirstLogin = false;

}
