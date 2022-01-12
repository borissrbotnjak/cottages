package com.isa.cottages.Service;

import com.isa.cottages.Model.AdventureReservation;

import java.util.List;

public interface FishingInstructorAdventureReservationService {


    List<AdventureReservation> getInstructorsUpcomingReservations(Long id) throws Exception;
}
