package com.isa.cottages.Service;

import com.isa.cottages.Model.Boat;
import com.isa.cottages.Model.BoatReservation;
import com.isa.cottages.Model.Client;

import java.time.LocalDate;
import java.util.List;

public interface BoatReservationService {

    List<BoatReservation> getPastReservations() throws Exception;
    List<BoatReservation> getUpcomingReservations() throws Exception;
    List<BoatReservation> getAllUpcoming();
    List<BoatReservation> findAllByClient(Client client);

    BoatReservation save(BoatReservation reservation);
    BoatReservation makeReservation(BoatReservation reservation, Boat boat) throws Exception;

    void setDate(BoatReservation boatReservation);
    void sendReservationMail(BoatReservation reservation);

    List<BoatReservation> findByOrderByStartTimeAsc() throws Exception;
    List<BoatReservation> findByOrderByStartTimeDesc() throws Exception;
    List<BoatReservation> findByOrderByDurationAsc() throws Exception;
    List<BoatReservation> findByOrderByDurationDesc() throws Exception;
    List<BoatReservation> findByOrderByPriceAsc() throws Exception;
    List<BoatReservation> findByOrderByPriceDesc() throws Exception;
    List<BoatReservation> findAllAvailable(LocalDate startTime, LocalDate endTime);
}
