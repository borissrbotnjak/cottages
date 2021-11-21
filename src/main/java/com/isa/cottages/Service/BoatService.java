package com.isa.cottages.Service;

import com.isa.cottages.Model.Boat;

import java.util.Collection;
import java.util.List;

public interface BoatService {

    Boat findById(Long id) throws Exception;

    Boat saveBoat(Boat boat);

    Collection<Boat> getAll();

    List<Boat> findByKeyword(String keyword);
}
