package com.isa.cottages.Service;

import com.isa.cottages.Model.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

public interface InstructorReservationsService {

    List<InstructorReservation> getPastReservations() throws Exception;
    List<InstructorReservation> getUpcomingReservations() throws Exception;
    List<InstructorReservation> findAllByClient(Client client);
    List<InstructorReservation> getAllUpcoming();

    List<InstructorReservation> findByOrderByStartTimeAsc() throws Exception;
    List<InstructorReservation> findByOrderByStartTimeDesc() throws Exception;
    List<InstructorReservation> findByOrderByDurationAsc() throws Exception;
    List<InstructorReservation> findByOrderByDurationDesc() throws Exception;
    List<InstructorReservation> findByOrderByPriceAsc() throws Exception;

    Double CalculatePrice(InstructorReservation reservation) throws ParseException;

    // Integer getDuration(InstructorReservation reservation) throws ParseException;

    Boolean canCancel(Long id);

    void cancel(Long id);

    InstructorReservation update(InstructorReservation reservation);

    InstructorReservation getOne(Long id);

    InstructorReservation save(InstructorReservation instructorReservation);

    void sendReservationMail(InstructorReservation reservation);

    List<InstructorReservation> findByOrderByPriceDesc() throws Exception;

    void setDate(Reservation reservation);

    InstructorReservation makeReservation(InstructorReservation reservation, FishingInstructorAdventure instructor) throws Exception;

    List<InstructorReservation> getAllWithDiscount(Long instructorId);

    InstructorReservation makeReservationOnDiscount(Long id) throws Exception;

    List<InstructorReservation> getAllUnavailable(LocalDate desiredStart, LocalDate desiredEnd);

    List<InstructorReservation> getAllAvailable(LocalDate desiredStart, LocalDate desiredEnd, int capacity);
}
