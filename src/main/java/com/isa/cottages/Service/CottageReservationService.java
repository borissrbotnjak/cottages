package com.isa.cottages.Service;

import com.isa.cottages.Model.BoatReservation;
import com.isa.cottages.Model.Client;
import com.isa.cottages.Model.CottageReservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface CottageReservationService {

    CottageReservation findOne(Long id);
    List<CottageReservation> getAllOwnersReservations(Long id) throws Exception;
    List<CottageReservation> getAllOwnersUpcomingReservations(Long id) throws Exception;
    List<CottageReservation> getOwnersUpcomingReservations(Long id) throws Exception;
    List<CottageReservation> getOwnersPastReservations(Long id) throws Exception;
    List<CottageReservation> getUpcomingReservations() throws Exception;
    List<CottageReservation> getPastReservations() throws Exception;

    CottageReservation saveDiscount(CottageReservation cottageReservation);
    List<CottageReservation> findDiscountsByCottage(Long id) throws Exception;

    List<CottageReservation> findAllByClient(Client client);
    List<CottageReservation> findClient(String keyword) throws Exception;
    Set<CottageReservation> findByInterval(LocalDate startDate, LocalDate endDate, Long id) throws Exception;

    List<CottageReservation> findByOrderByStartTimeAsc() throws Exception;
    List<CottageReservation> findByOrderByStartTimeDesc() throws Exception;
    List<CottageReservation> findByOrderByDurationAsc() throws Exception;
    List<CottageReservation> findByOrderByDurationDesc() throws Exception;
    List<CottageReservation> findByOrderByPriceAsc() throws Exception;
    List<CottageReservation> findByOrderByPriceDesc() throws Exception;
}
