package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.*;
import com.isa.cottages.Repository.BoatReservationRepository;
import com.isa.cottages.Service.BoatReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class BoatReservationServiceImpl implements BoatReservationService {

    private ClientServiceImpl clientService;
    private UserServiceImpl userService;
    private BoatReservationRepository reservationRepository;
    private BoatServiceImpl boatService;

    @Autowired
    public BoatReservationServiceImpl(ClientServiceImpl clientService, UserServiceImpl userService,
                                      BoatReservationRepository boatReservationRepository,
                                      BoatServiceImpl boatService) {
        this.clientService = clientService;
        this.userService = userService;
        this.reservationRepository = boatReservationRepository;
        this.boatService = boatService;
    }

    @Override
    public List<BoatReservation> getAllOwnersReservations(Long id) throws Exception {
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();

        return this.reservationRepository.getAllOwnersReservations(id);
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
    public List<BoatReservation> findByOrderByPriceDesc() throws Exception {
        List<BoatReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(BoatReservation::getPrice).reversed());

        return pastOnes;
    }

    @Override
    public List<BoatReservation> findAllByClient(Client client) { return this.reservationRepository.findAllByClient(client.getId()); }

    @Override
    public List<BoatReservation> getOwnersUpcomingReservations(Long id) throws Exception {
       BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();
        List<BoatReservation> all = this.reservationRepository.getAllReservedByOwner(id);
        List<BoatReservation> upcoming = new ArrayList<>();

        for (BoatReservation res: all) {
            if((res.getStartTime().isAfter(LocalDateTime.now())) && (Objects.equals(res.getBoatOwner().getId(), boatOwner.getId()))) {
                upcoming.add(res);
            }
        }
        return upcoming;
    }

    @Override
    public List<BoatReservation> getOwnersPastReservations(Long id) throws Exception {
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();
//        List<Boat> boat = this.boatService.findByCottageOwner(id);
        List<BoatReservation> all = this.reservationRepository.getAllReservedByOwner(id);
        List<BoatReservation> pastOnes = new ArrayList<>();

        for (BoatReservation res:all) {
            if((res.getStartTime().isBefore(LocalDateTime.now())) && (Objects.equals(res.getBoatOwner().getId(), boatOwner.getId()))) {
                pastOnes.add(res);
            }
        }
        return pastOnes;
    }

    @Override
    public List<BoatReservation> getOwnersFreeReservations(Long id) throws Exception {
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();
        List<BoatReservation> all = this.reservationRepository.getAllFreeReservationsByOwner(id);

        return all;
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

    @Override
    public BoatReservation saveDiscount(BoatReservation boatReservation) {
        BoatReservation br = new BoatReservation();

        br.setDiscountAvailableFrom(boatReservation.getDiscountAvailableFrom());
        br.setDiscountAvailableUntil(boatReservation.getDiscountAvailableUntil());
        br.setNumPersons(boatReservation.getNumPersons());
        br.setPrice(boatReservation.getPrice());
        br.setAdditionalServices(boatReservation.getAdditionalServices());
        br.setBoatOwner(boatReservation.getBoatOwner());
        br.setBoat(boatReservation.getBoat());
        br.setDiscount(true);
        br.setDeleted(false);
        br.setReserved(false);
        br.setClient(boatReservation.getClient());
        this.reservationRepository.save(br);

        return br;
    }

    @Override
    public BoatReservation saveReservation(BoatReservation boatReservation) {
        BoatReservation br = new BoatReservation();

        br.setNumPersons(boatReservation.getNumPersons());
        br.setPrice(boatReservation.getPrice());
        br.setAdditionalServices(boatReservation.getAdditionalServices());
        br.setBoatOwner(boatReservation.getBoatOwner());
        br.setBoat(boatReservation.getBoat());
        br.setDiscount(false);
        br.setDeleted(false);
        br.setReserved(false);
        br.setClient(boatReservation.getClient());
        this.reservationRepository.save(br);

        return br;
    }

    @Override
    public List<BoatReservation> findDiscountsByBoat(Long id) throws Exception{
        Boat boat = boatService.findById(id);
        List<BoatReservation> all = this.reservationRepository.findDiscountsByBoat(id);
        List<BoatReservation> br = new ArrayList<>();

        for (BoatReservation b:all) {
            if(Objects.equals(b.getBoat().getId(), boat.getId())) {
                br.add(b);
            }
        }
        return br;
    }

    @Override
    public List<BoatReservation> findClient(String keyword) throws Exception {
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();

        return this.reservationRepository.findClient(keyword);
    }
}


