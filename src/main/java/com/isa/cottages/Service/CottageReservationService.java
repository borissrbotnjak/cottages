package com.isa.cottages.Service;

import com.isa.cottages.Model.CottageReservation;

import java.util.List;

public interface CottageReservationService {

    List<CottageReservation> getAllOwnersReservations(Long id) throws Exception;

    List<CottageReservation> getOwnersUpcomingReservations(Long id) throws Exception;
    List<CottageReservation> getOwnersPastReservations(Long id) throws Exception;
    List<CottageReservation> getUpcomingReservations() throws Exception;
    List<CottageReservation> getPastReservations() throws Exception;

    CottageReservation saveDiscount(CottageReservation cottageReservation);
    List<CottageReservation> findDiscountsByCottage(Long id) throws Exception;

    List<CottageReservation> findClient(String keyword) throws Exception;
}
