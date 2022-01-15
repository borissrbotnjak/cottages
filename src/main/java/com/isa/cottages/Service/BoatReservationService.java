package com.isa.cottages.Service;

import com.isa.cottages.Model.Boat;
import com.isa.cottages.Model.BoatReservation;
import com.isa.cottages.Model.Client;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BoatReservationService {

    BoatReservation findById(Long id) throws Exception;
    List<BoatReservation> getAllOwnersReservations(Long id) throws Exception;
    List<BoatReservation> getAllOwnersReservedReservations(Long id) throws Exception;
    List<BoatReservation> getOwnersUpcomingReservations(Long id) throws Exception;
    List<BoatReservation> getAllOwnersUpcomingReservations(Long id) throws Exception;
    List<BoatReservation> getOwnersPastReservations(Long id) throws Exception;
    List<BoatReservation> getOwnersFreeReservations(Long id) throws Exception;
    List<BoatReservation> getPastReservations() throws Exception;
    List<BoatReservation> getUpcomingReservations() throws Exception;
    List<BoatReservation> getAllUpcoming();
    List<BoatReservation> findAllByClient(Client client);
    List<BoatReservation> findClient(String keyword) throws Exception;
    Set<BoatReservation> findByInterval(LocalDate startDate, LocalDate endDate, Long id) throws Exception;

    BoatReservation saveDiscount(BoatReservation boatReservation);
    BoatReservation saveReservation(BoatReservation boatReservation);
    List<BoatReservation> findDiscountsByBoat(Long id) throws Exception;

    BoatReservation save(BoatReservation reservation);
    BoatReservation makeReservation(BoatReservation reservation, Boat boat) throws Exception;
    BoatReservation makeReservationOnDiscount(Long reservationId) throws Exception;

    Boolean canCancel(Long id);

    void cancel(Long id);
    void deleteById(Long id);
    void setDate(BoatReservation boatReservation);
    void sendReservationMail(BoatReservation reservation);

    List<BoatReservation> findByOrderByStartTimeAsc() throws Exception;
    List<BoatReservation> findByOrderByStartTimeDesc() throws Exception;
    List<BoatReservation> findByOrderByDurationAsc() throws Exception;
    List<BoatReservation> findByOrderByDurationDesc() throws Exception;
    List<BoatReservation> findByOrderByPriceAsc() throws Exception;
    List<BoatReservation> findByOrderByPriceDesc() throws Exception;
    List<BoatReservation> findAllAvailable(LocalDate startTime, LocalDate endTime);

    List<BoatReservation> getAllWithDiscount(Long boatId);

    BoatReservation update(BoatReservation reservation);

    BoatReservation save(BoatReservation reservation);
    BoatReservation getOne(Long id);

    BoatReservation makeReservationWithClient(BoatReservation reservation, Boat boat, Long clid) throws Exception;

}
