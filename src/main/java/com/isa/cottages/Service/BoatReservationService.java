package com.isa.cottages.Service;

import com.isa.cottages.Model.BoatReservation;
import com.isa.cottages.Model.Client;
import com.isa.cottages.Model.CottageReservation;

import java.util.List;

public interface BoatReservationService {

    List<BoatReservation> getAllOwnersReservations(Long id) throws Exception;
    List<BoatReservation> getOwnersUpcomingReservations(Long id) throws Exception;
    List<BoatReservation> getAllOwnersUpcomingReservations(Long id) throws Exception;
    List<BoatReservation> getOwnersPastReservations(Long id) throws Exception;
    List<BoatReservation> getOwnersFreeReservations(Long id) throws Exception;
    List<BoatReservation> getPastReservations() throws Exception;
    List<BoatReservation> getUpcomingReservations() throws Exception;
    List<BoatReservation> findAllByClient(Client client);
    List<BoatReservation> findClient(String keyword) throws Exception;

    BoatReservation saveDiscount(BoatReservation boatReservation);
    BoatReservation saveReservation(BoatReservation boatReservation);
    List<BoatReservation> findDiscountsByBoat(Long id) throws Exception;

    List<BoatReservation> findByOrderByStartTimeAsc() throws Exception;
    List<BoatReservation> findByOrderByStartTimeDesc() throws Exception;
    List<BoatReservation> findByOrderByDurationAsc() throws Exception;
    List<BoatReservation> findByOrderByDurationDesc() throws Exception;
    List<BoatReservation> findByOrderByPriceAsc() throws Exception;
    List<BoatReservation> findByOrderByPriceDesc() throws Exception;
}
