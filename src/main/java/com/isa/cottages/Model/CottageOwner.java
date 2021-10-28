package com.isa.cottages.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("CottageOwner")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CottageOwner extends User {

    @OneToMany(mappedBy = "cottageOwner", cascade = CascadeType.ALL)
    private Set<Cottage> cottages = new HashSet<>();

}
