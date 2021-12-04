package com.isa.cottages.Service;

import com.isa.cottages.Model.BoatReservation;
import com.isa.cottages.Model.Client;

import java.util.List;

public interface BoatReservationService {

    List<BoatReservation> getPastReservations() throws Exception;
    List<BoatReservation> getUpcomingReservations() throws Exception;
    List<BoatReservation> findAllByClient(Client client);
}
