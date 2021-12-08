package com.isa.cottages.Service;

import com.isa.cottages.Model.Client;
import com.isa.cottages.Model.CottageReservation;
import com.isa.cottages.Model.InstructorReservation;

import java.util.List;

public interface CottageReservationService {

    List<CottageReservation> getAllOwnersReservations(Long id) throws Exception;

    List<CottageReservation> getOwnersUpcomingReservations(Long id) throws Exception;
    List<CottageReservation> getOwnersPastReservations(Long id) throws Exception;
    List<CottageReservation> getUpcomingReservations() throws Exception;
    List<CottageReservation> getPastReservations() throws Exception;

    CottageReservation saveDiscount(CottageReservation cottageReservation);
    List<CottageReservation> findDiscountsByCottage(Long id) throws Exception;

    List<CottageReservation> findAllByClient(Client client);
    List<CottageReservation> findClient(String keyword) throws Exception;

    List<CottageReservation> findByOrderByStartTimeAsc() throws Exception;
    List<CottageReservation> findByOrderByStartTimeDesc() throws Exception;
    List<CottageReservation> findByOrderByDurationAsc() throws Exception;
    List<CottageReservation> findByOrderByDurationDesc() throws Exception;
    List<CottageReservation> findByOrderByPriceAsc() throws Exception;
    List<CottageReservation> findByOrderByPriceDesc() throws Exception;
}
