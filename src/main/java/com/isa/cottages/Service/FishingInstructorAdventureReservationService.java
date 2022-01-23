package com.isa.cottages.Service;

import com.isa.cottages.Model.AdventureReservation;
import com.isa.cottages.Model.FishingInstructorAdventure;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface FishingInstructorAdventureReservationService {


    List<AdventureReservation> getInstructorsUpcomingReservations(Long id) throws Exception;

    List<AdventureReservation> getAllMyAvailable(LocalDate desiredStart, LocalDate desiredEnd, int capacity, Long id) throws Exception;

    List<AdventureReservation> getAllMyUnavailable(LocalDate desiredStart, LocalDate desiredEnd, Long id) throws Exception;

    void sendReservationMail(AdventureReservation reservation);

    List<AdventureReservation> findByOrderByStartTimeAsc() throws Exception;

    List<AdventureReservation> findByOrderByStartTimeDesc() throws Exception;

    List<AdventureReservation> findByOrderByDurationAsc() throws Exception;

    List<AdventureReservation> findByOrderByDurationDesc() throws Exception;

    List<AdventureReservation> findByOrderByPriceAsc() throws Exception;

    List<AdventureReservation> findByOrderByPriceDesc() throws Exception;

    AdventureReservation update(AdventureReservation reservation);

    AdventureReservation getOne(Long id);

    AdventureReservation makeReservationOnDiscount(Long reservationId) throws Exception;

    Boolean canCancel(Long id);

    void cancel(Long id);

    List<AdventureReservation> getUpcomingReservations() throws Exception;

    List<AdventureReservation> getAllUpcoming();

    List<AdventureReservation> getPastReservations() throws Exception;

    List<AdventureReservation> findClient(String keyword) throws Exception;

    List<AdventureReservation> getInstructorsPastReservations(Long id) throws Exception;

    Set<AdventureReservation> findByInterval(LocalDate startDate, LocalDate endDate, Long id) throws Exception;

    Set<AdventureReservation> findByInterval2(LocalDate startDate, LocalDate endDate, Long id) throws Exception;

    List<AdventureReservation> getAllInstructorsNowAndUpcomingReservations(Long id) throws Exception;

    List<AdventureReservation> findDiscountsByAdventure(Long id);

    AdventureReservation saveDiscount(AdventureReservation adventureReservation);

    void setDate(AdventureReservation reservation);

    AdventureReservation save(AdventureReservation reservation);

    List<AdventureReservation> getAllWithDiscount(Long AdventureId);

    AdventureReservation makeReservationWithClient(AdventureReservation reservation, FishingInstructorAdventure adventure, Long clid) throws Exception;
}
