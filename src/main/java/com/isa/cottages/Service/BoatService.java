package com.isa.cottages.Service;

import com.isa.cottages.Model.Boat;
import com.isa.cottages.Model.Cottage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface BoatService {

    Boat findById(Long id) throws Exception;

    Boat saveBoat(Boat boat);

    Collection<Boat> getAll();
    List<Boat> findByKeyword(String keyword);
    List<Boat> orderByNameDesc();
    List<Boat> orderByNameAsc();
    List<Boat> orderByRatingAsc();
    List<Boat> orderByRatingDesc();
    List<Boat> orderByAddressDesc();
    List<Boat> orderByAddressAsc();

    Boolean boatAvailable(LocalDate startDate, LocalDate endDate, Boat boat, int numPersons);

    Set<Boat> findAllAvailable(LocalDate startDate, LocalDate endDate, int numOfPersons) throws Exception;
}
