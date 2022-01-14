package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.AdventureReservation;
import com.isa.cottages.Model.Instructor;
import com.isa.cottages.Repository.FishingInstructorAdventureReservationRepository;
import com.isa.cottages.Service.FishingInstructorAdventureReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FishingInstructorAdventureReservationServiceImpl implements FishingInstructorAdventureReservationService {
    private final FishingInstructorAdventureReservationRepository reservationRepository;
    private final UserServiceImpl userService;

    @Autowired
    public FishingInstructorAdventureReservationServiceImpl(FishingInstructorAdventureReservationRepository reservationRepository, UserServiceImpl userService) {
        this.reservationRepository = reservationRepository;
        this.userService = userService;
    }

    @Override
    public List<AdventureReservation> getInstructorsUpcomingReservations(Long id) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        List<AdventureReservation> all = this.reservationRepository.getAllReservedByInstructor(id);
        List<AdventureReservation> upcoming = new ArrayList<>();

        for (AdventureReservation res : all)
            if ((res.getStartTime().isAfter(LocalDateTime.now())) && (Objects.equals(res.getInstructor().getId(), instructor.getId())))
                upcoming.add(res);
        return upcoming;
    }

    @Override
    public List<AdventureReservation> findClient(String keyword) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();

        return this.reservationRepository.findClient(keyword);
    }

    @Override
    public List<AdventureReservation> getInstructorsPastReservations(Long id) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        List<AdventureReservation> all = this.reservationRepository.getAllReservedByInstructor(id);
        List<AdventureReservation> pastOnes = new ArrayList<>();

        for (AdventureReservation res:all) {
            if((res.getStartTime().isBefore(LocalDateTime.now())) && (Objects.equals(res.getInstructor().getId(), instructor.getId()))) {
                pastOnes.add(res);
            }
        }
        return pastOnes;
    }
}
