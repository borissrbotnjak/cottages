package com.isa.cottages.Service;

import com.isa.cottages.Model.BoatReservation;
import com.isa.cottages.Model.Client;
import com.isa.cottages.Model.InstructorReservation;

import java.util.List;

public interface InstructorReservationsService {

    List<InstructorReservation> getPastReservations() throws Exception;
    List<InstructorReservation> getUpcomingReservations() throws Exception;
    List<InstructorReservation> findAllByClient(Client client);

    List<InstructorReservation> findByOrderByStartTimeAsc() throws Exception;
    List<InstructorReservation> findByOrderByStartTimeDesc() throws Exception;
    List<InstructorReservation> findByOrderByDurationAsc() throws Exception;
    List<InstructorReservation> findByOrderByDurationDesc() throws Exception;
    List<InstructorReservation> findByOrderByPriceAsc() throws Exception;
    List<InstructorReservation> findByOrderByPriceDesc() throws Exception;
}
