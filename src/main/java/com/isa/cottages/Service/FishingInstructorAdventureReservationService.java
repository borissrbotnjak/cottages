package com.isa.cottages.Service;

import com.isa.cottages.Model.AdventureReservation;

import java.util.List;

public interface FishingInstructorAdventureReservationService {


    List<AdventureReservation> getInstructorsUpcomingReservations(Long id) throws Exception;

    List<AdventureReservation> findClient(String keyword) throws Exception;

    List<AdventureReservation> getInstructorsPastReservations(Long id) throws Exception;
}
