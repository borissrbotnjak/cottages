package com.isa.cottages.Service;

import com.isa.cottages.Model.Boat;
import com.isa.cottages.Model.Cottage;

import java.util.Collection;
import java.util.List;

public interface BoatService {

    Boat findById(Long id) throws Exception;

    Boat saveBoat(Boat boat);

    Collection<Boat> getAll();

    List<Boat> findByBoatOwner(Long id) throws Exception;
    List<Boat> findByKeyword(String keyword);

    List<Boat> orderByNameDesc();
    List<Boat> orderByNameAsc();
    List<Boat> orderByRatingAsc();
    List<Boat> orderByRatingDesc();
    List<Boat> orderByAddressDesc();
    List<Boat> orderByAddressAsc();
}
