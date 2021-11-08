package com.isa.cottages.Service;

import com.isa.cottages.Model.CottageReservation;

import java.util.List;

public interface CottageReservationService {

//    List<Reservation> findReserved(Long id);

    List<CottageReservation> getUpcomingReservations() throws Exception;

    List<CottageReservation> getPastReservations() throws Exception;
}
