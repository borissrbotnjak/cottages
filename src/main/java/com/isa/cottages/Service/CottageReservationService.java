package com.isa.cottages.Service;

import com.isa.cottages.Model.CottageReservation;

import java.util.List;

public interface CottageReservationService {

    List<CottageReservation> getUpcomingReservations() throws Exception;

    List<CottageReservation> getPastReservations() throws Exception;
}
