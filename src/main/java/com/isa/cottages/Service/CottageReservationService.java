package com.isa.cottages.Service;

import com.isa.cottages.Model.CottageReservation;

import java.util.List;

public interface CottageReservationService {

    List<CottageReservation> getUpcomingReservations() throws Exception;

    List<CottageReservation> getPastReservations() throws Exception;

    CottageReservation saveAction(CottageReservation cottageReservation) throws Exception;
    List<CottageReservation> findActionsByCottage(Long id) throws Exception;

    List<CottageReservation> findClient(String keyword);
}
