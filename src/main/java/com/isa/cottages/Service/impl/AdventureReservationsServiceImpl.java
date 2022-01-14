package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.*;
import com.isa.cottages.Repository.AdventureReservationRepository;
import com.isa.cottages.Service.AdventureReservationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class AdventureReservationsServiceImpl implements AdventureReservationsService {

    private ClientServiceImpl clientService;
    private UserServiceImpl userService;
    private AdventureReservationRepository reservationRepository;

    @Autowired
    public AdventureReservationsServiceImpl(ClientServiceImpl clientService, UserServiceImpl userService, AdventureReservationRepository adventureReservationRepository) {
        this.clientService = clientService;
        this.userService = userService;
        this.reservationRepository = adventureReservationRepository;
    }

    @Override
    public List<AdventureReservation> getPastReservations() throws Exception {
        Client cl = this.clientService.findByEmail(this.userService.getUserFromPrincipal().getEmail());
        List<AdventureReservation> all = this.reservationRepository.getAllReservations();
        List<AdventureReservation> pastOnes = new ArrayList<>();

        for (AdventureReservation res : all) {
            if ((res.getStartTime().isBefore(LocalDateTime.now())) && (res.getEndTime().isBefore(LocalDateTime.now()))
                    && (Objects.equals(res.getClient().getId(), cl.getId()))) {
                pastOnes.add(res);
            }
        }

        return pastOnes;
    }

    @Override
    public List<AdventureReservation> getUpcomingReservations() throws Exception {
        Client cl = this.clientService.findByEmail(this.userService.getUserFromPrincipal().getEmail());
        List<AdventureReservation> all = this.reservationRepository.getAllReservations();
        List<AdventureReservation> upcoming = new ArrayList<>();

        for (AdventureReservation res : all) {
            if ((res.getStartTime().isAfter(LocalDateTime.now())) && (res.getEndTime().isAfter(LocalDateTime.now()))
                    && (Objects.equals(res.getClient().getId(), cl.getId()))) {
                upcoming.add(res);
            }
        }
        return upcoming;
    }

    @Override
    public List<AdventureReservation> findByOrderByStartTimeAsc() throws Exception {
        List<AdventureReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(AdventureReservation::getStartTime));

        return pastOnes;
    }

    @Override
    public List<AdventureReservation> findByOrderByStartTimeDesc() throws Exception {
        List<AdventureReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(AdventureReservation::getStartTime).reversed());

        return pastOnes;
    }

    @Override
    public List<AdventureReservation> findByOrderByDurationAsc() throws Exception {
        List<AdventureReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(AdventureReservation::getDuration));

        return pastOnes;
    }

    @Override
    public List<AdventureReservation> findByOrderByDurationDesc() throws Exception {
        List<AdventureReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(AdventureReservation::getDuration).reversed());

        return pastOnes;
    }

    @Override
    public List<AdventureReservation> findByOrderByPriceAsc() throws Exception {
        List<AdventureReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(AdventureReservation::getPrice));

        return pastOnes;
    }

    @Override
    public List<AdventureReservation> findByOrderByPriceDesc() throws Exception {
        List<AdventureReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(AdventureReservation::getPrice).reversed());

        return pastOnes;
    }

    @Override
    public List<AdventureReservation> findAllByClient(Client client) { return this.reservationRepository.findAllByClient(client.getId()); }
}
