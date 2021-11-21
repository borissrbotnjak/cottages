package com.isa.cottages.Service;

import com.isa.cottages.Model.CottageReservation;

import java.util.List;

public interface CottageReservationService {

    List<CottageReservation> getAllReservations(Long id) throws Exception;

    List<CottageReservation> getUpcomingReservations(Long id) throws Exception;
    List<CottageReservation> getPastReservations(Long id) throws Exception;

    CottageReservation saveDiscount(CottageReservation cottageReservation) throws Exception;
    List<CottageReservation> findDiscountsByCottage(Long id) throws Exception;

    List<CottageReservation> findClient(String keyword) throws Exception;
}
