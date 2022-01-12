package com.isa.cottages.Service;

import com.isa.cottages.Model.Boat;

import java.util.Collection;
import java.util.List;

public interface BoatService {

    Collection<Boat> getAll();
    Boat findById(Long id) throws Exception;
    List<Boat> findByBoatOwner(Long id) throws Exception;
    List<Boat> findByKeyword(String keyword);

    Boat saveBoat(Boat boat);
    Boat updateBoat(Boat boat) throws Exception;
    void removeBoat(Boat boat, Long id) throws Exception;
    Boolean canUpdateOrDelete(Long id) throws Exception;

    Boat defineAvailability(Boat boat) throws Exception;

    List<Boat> orderByNameDesc();
    List<Boat> orderByNameAsc();
    List<Boat> orderByRatingAsc();
    List<Boat> orderByRatingDesc();
    List<Boat> orderByAddressDesc();
    List<Boat> orderByAddressAsc();
}
