package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.Client;
import com.isa.cottages.Model.InstructorReservation;
import com.isa.cottages.Repository.InstructorReservationRepository;
import com.isa.cottages.Service.InstructorReservationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class InstructorReservationsServiceImpl implements InstructorReservationsService {

    private ClientServiceImpl clientService;
    private UserServiceImpl userService;
    private InstructorReservationRepository reservationRepository;

    @Autowired
    public InstructorReservationsServiceImpl(ClientServiceImpl clientService, UserServiceImpl userService, InstructorReservationRepository instructorReservationRepository) {
        this.clientService = clientService;
        this.userService = userService;
        this.reservationRepository = instructorReservationRepository;
    }

    @Override
    public List<InstructorReservation> getPastReservations() throws Exception {
        Client cl = this.clientService.findByEmail(this.userService.getUserFromPrincipal().getEmail());
        List<InstructorReservation> all = this.reservationRepository.getAllReservations();
        List<InstructorReservation> pastOnes = new ArrayList<>();

        for (InstructorReservation res : all) {
            if ((res.getStartTime().isBefore(LocalDateTime.now())) && (res.getEndTime().isBefore(LocalDateTime.now()))
                    && (Objects.equals(res.getClient().getId(), cl.getId()))) {
                pastOnes.add(res);
            }
        }
        return pastOnes;
    }

    @Override
    public List<InstructorReservation> getUpcomingReservations() throws Exception {
        Client cl = this.clientService.findByEmail(this.userService.getUserFromPrincipal().getEmail());
        List<InstructorReservation> all = this.reservationRepository.getAllReservations();
        List<InstructorReservation> upcoming = new ArrayList<>();

        for (InstructorReservation res : all) {
            if ((res.getStartTime().isAfter(LocalDateTime.now())) && (res.getEndTime().isAfter(LocalDateTime.now()))
                    && (Objects.equals(res.getClient().getId(), cl.getId()))) {
                upcoming.add(res);
            }
        }
        return upcoming;
    }
}
