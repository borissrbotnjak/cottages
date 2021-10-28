package com.isa.cottages.Service;

import com.isa.cottages.Model.Boat;

public interface BoatService {

    Boat findById(Long id) throws Exception;

    Boat saveBoat(Boat boat);
}
