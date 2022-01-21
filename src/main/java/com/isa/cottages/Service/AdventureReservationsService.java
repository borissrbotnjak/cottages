package com.isa.cottages.Service;

import com.isa.cottages.Model.AdventureReservation;
import com.isa.cottages.Model.Client;

import java.util.List;

public interface AdventureReservationsService {

    List<AdventureReservation> getPastReservations() throws Exception;
    List<AdventureReservation> getUpcomingReservations() throws Exception;
    List<AdventureReservation> findAllByClient(Client client);

    List<AdventureReservation> getAllInstructorsReservations(Long id) throws Exception;

    List<AdventureReservation> findByOrderByStartTimeAsc() throws Exception;
    List<AdventureReservation> findByOrderByStartTimeDesc() throws Exception;
    List<AdventureReservation> findByOrderByDurationAsc() throws Exception;
    List<AdventureReservation> findByOrderByDurationDesc() throws Exception;
    List<AdventureReservation> findByOrderByPriceAsc() throws Exception;
    List<AdventureReservation> findByOrderByPriceDesc() throws Exception;
}
