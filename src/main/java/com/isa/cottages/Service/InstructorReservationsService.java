package com.isa.cottages.Service;

import com.isa.cottages.Model.Client;
import com.isa.cottages.Model.InstructorReservation;

import java.util.List;

public interface InstructorReservationsService {

    List<InstructorReservation> getPastReservations() throws Exception;
    List<InstructorReservation> getUpcomingReservations() throws Exception;
    List<InstructorReservation> findAllByClient(Client client);
}
