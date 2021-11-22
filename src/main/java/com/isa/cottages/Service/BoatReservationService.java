package com.isa.cottages.Service;

import com.isa.cottages.Model.CottageReservation;

import java.util.List;

public interface BoatReservationService {

    List<CottageReservation> getPastBoatReservations() throws Exception;
}
