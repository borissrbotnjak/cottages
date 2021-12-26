package com.isa.cottages.Service;

import com.isa.cottages.Model.BoatReservation;
import com.isa.cottages.Model.Client;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BoatReservationService {

    List<BoatReservation> getPastReservations() throws Exception;
    List<BoatReservation> getUpcomingReservations() throws Exception;
    List<BoatReservation> getAllUpcoming() throws Exception;
    List<BoatReservation> findAllByClient(Client client);

    List<BoatReservation> findByOrderByStartTimeAsc() throws Exception;
    List<BoatReservation> findByOrderByStartTimeDesc() throws Exception;
    List<BoatReservation> findByOrderByDurationAsc() throws Exception;
    List<BoatReservation> findByOrderByDurationDesc() throws Exception;
    List<BoatReservation> findByOrderByPriceAsc() throws Exception;
    List<BoatReservation> findByOrderByPriceDesc() throws Exception;
    List<BoatReservation> findAllAvailable(LocalDate startTime, LocalDate endTime);
    List<BoatReservation> findAvailable_2(LocalDate startTime, LocalDate endTime) throws Exception;
}
