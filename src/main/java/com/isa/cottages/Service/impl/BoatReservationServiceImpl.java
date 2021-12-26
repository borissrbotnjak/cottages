package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.BoatReservation;
import com.isa.cottages.Model.Client;
import com.isa.cottages.Repository.BoatReservationRepository;
import com.isa.cottages.Service.BoatReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class BoatReservationServiceImpl implements BoatReservationService {

    private ClientServiceImpl clientService;
    private UserServiceImpl userService;
    private BoatReservationRepository reservationRepository;

    @Autowired
    public BoatReservationServiceImpl(ClientServiceImpl clientService, UserServiceImpl userService, BoatReservationRepository boatReservationRepository) {
        this.clientService = clientService;
        this.userService = userService;
        this.reservationRepository = boatReservationRepository;
    }

    @Override
    public List<BoatReservation> findByOrderByStartTimeAsc() throws Exception {
        List<BoatReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(BoatReservation::getStartTime));

        return pastOnes;
    }

    @Override
    public List<BoatReservation> findByOrderByStartTimeDesc() throws Exception {
        List<BoatReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(BoatReservation::getStartTime).reversed());

        return pastOnes;
    }

    @Override
    public List<BoatReservation> findByOrderByDurationAsc() throws Exception {
        List<BoatReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(BoatReservation::getDuration));

        return pastOnes;
    }

    @Override
    public List<BoatReservation> findByOrderByDurationDesc() throws Exception {
        List<BoatReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(BoatReservation::getDuration).reversed());

        return pastOnes;
    }

    @Override
    public List<BoatReservation> findByOrderByPriceAsc() throws Exception {
        List<BoatReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(BoatReservation::getPrice));

        return pastOnes;
    }

    @Override
    public List<BoatReservation> findAllAvailable(LocalDate startTime, LocalDate endTime) {
        List<BoatReservation> all = this.reservationRepository.getAllReservations();
        List<BoatReservation> available = new ArrayList<>();

        for (BoatReservation res : all) {
            if ((res.getStartTime().isAfter(ChronoLocalDateTime.from(startTime)) && res.getEndTime().isAfter(ChronoLocalDateTime.from(endTime))) ||
                            (res.getStartTime() == null && res.getEndTime() == null) ||
                            (res.getStartTime().isBefore(ChronoLocalDateTime.from(startTime)) && res.getEndTime().isBefore(ChronoLocalDateTime.from(endTime)))) {
                available.add(res);
            }
        }
        return available;
    }

    @Override
    public List<BoatReservation> getAllUpcoming() {
        List<BoatReservation> all = this.reservationRepository.getAllReservations();
        List<BoatReservation> upcoming = new ArrayList<>();

        for (BoatReservation res : all) {
            if ((res.getStartTime().isAfter(LocalDateTime.now())) && (res.getEndTime().isAfter(LocalDateTime.now()))) {
                upcoming.add(res);
            }
        }
        return upcoming;
    }

    @Override
    public List<BoatReservation> findAvailable_2(LocalDate startTime, LocalDate endTime) throws Exception {
        List<BoatReservation> all = getAllUpcoming();
        List<BoatReservation> available = new ArrayList<>();

        /*for (BoatReservation res : all) {
            if ((res.getStartTime().isAfter(ChronoLocalDateTime.from(startTime)) && res.getEndTime().isAfter(ChronoLocalDateTime.from(endTime))) ||
                    (res.getStartTime() == null && res.getEndTime() == null) ||
                    (res.getStartTime().isBefore(ChronoLocalDateTime.from(startTime)) && res.getEndTime().isBefore(ChronoLocalDateTime.from(endTime)))) {
                available.add(res);
            }
        }*/
        for (BoatReservation res : all) {
            if ((res.getStartDate().isAfter(startTime) && res.getEndDate().isAfter(endTime)) ||
                    (res.getStartDate() == null && res.getEndDate() == null) ||
                    (res.getStartDate().isBefore(startTime) && res.getEndDate().isBefore(endTime))) {
                available.add(res);
            }
        }
        return available;
    }

    @Override
    public List<BoatReservation> findByOrderByPriceDesc() throws Exception {
        List<BoatReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(BoatReservation::getPrice).reversed());

        return pastOnes;
    }

    @Override
    public List<BoatReservation> findAllByClient(Client client) {
        return this.reservationRepository.findAllByClient(client.getId());
    }

    @Override
    public List<BoatReservation> getPastReservations() throws Exception {
        Client cl = this.clientService.findByEmail(this.userService.getUserFromPrincipal().getEmail());
        List<BoatReservation> all = this.reservationRepository.getAllReservations();
        List<BoatReservation> pastOnes = new ArrayList<>();

        for (BoatReservation res : all) {
            if ((res.getStartTime().isBefore(LocalDateTime.now())) && (res.getEndTime().isBefore(LocalDateTime.now()))
                    && (Objects.equals(res.getClient().getId(), cl.getId()))) {
                pastOnes.add(res);
            }
        }
        return pastOnes;
    }

    @Override
    public List<BoatReservation> getUpcomingReservations() throws Exception {
        Client cl = this.clientService.findByEmail(this.userService.getUserFromPrincipal().getEmail());
        List<BoatReservation> all = this.reservationRepository.getAllReservations();
        List<BoatReservation> upcoming = new ArrayList<>();

        for (BoatReservation res : all) {
            if ((res.getStartTime().isAfter(LocalDateTime.now())) && (res.getEndTime().isAfter(LocalDateTime.now()))
                    && (Objects.equals(res.getClient().getId(), cl.getId()))) {
                upcoming.add(res);
            }
        }
        return upcoming;
    }
}
