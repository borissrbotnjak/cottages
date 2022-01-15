package com.isa.cottages.Service;

import com.isa.cottages.Model.*;

import java.util.List;

public interface CottageReservationService {

    List<CottageReservation> getAllOwnersReservations(Long id) throws Exception;

    Boolean canCancel(Long id);

    void cancel(Long id);

    List<CottageReservation> getOwnersUpcomingReservations(Long id) throws Exception;
    List<CottageReservation> getOwnersPastReservations(Long id) throws Exception;
    List<CottageReservation> getUpcomingReservations() throws Exception;
    List<CottageReservation> getPastReservations() throws Exception;
    List<CottageReservation> getAllUpcoming();

    CottageReservation saveDiscount(CottageReservation cottageReservation);
    CottageReservation save(CottageReservation cottageReservation);
    CottageReservation makeReservation(CottageReservation reservation, Cottage cottage) throws Exception;

    List<CottageReservation> findDiscountsByCottage(Long id) throws Exception;

    List<CottageReservation> findAllByClient(Client client);
    List<CottageReservation> findClient(String keyword) throws Exception;

    List<CottageReservation> findByOrderByStartTimeAsc() throws Exception;
    List<CottageReservation> findByOrderByStartTimeDesc() throws Exception;
    List<CottageReservation> findByOrderByDurationAsc() throws Exception;
    List<CottageReservation> findByOrderByDurationDesc() throws Exception;
    List<CottageReservation> findByOrderByPriceAsc() throws Exception;
    List<CottageReservation> findByOrderByPriceDesc() throws Exception;

    void setDate(Reservation reservation);
    void sendReservationMail(CottageReservation reservation);

    List<CottageReservation> getAllWithDiscount(Long CottageId);

    CottageReservation update(CottageReservation reservation);

    CottageReservation makeReservationOnDiscount(Long reservationId) throws Exception;

    CottageReservation getOne(Long id);
}
