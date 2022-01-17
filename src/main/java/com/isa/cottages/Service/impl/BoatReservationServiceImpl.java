package com.isa.cottages.Service.impl;

import com.isa.cottages.Email.EmailService;
import com.isa.cottages.Model.*;
import com.isa.cottages.Repository.BoatReservationRepository;
import com.isa.cottages.Service.BoatReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BoatReservationServiceImpl implements BoatReservationService {

    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private BoatReservationRepository reservationRepository;

    @Autowired
    private BoatServiceImpl boatService;

    @Autowired
    private EmailService emailService;

    @Override
    public BoatReservation findById(Long id) throws Exception{
        if (this.reservationRepository.findById(id).isEmpty()) {
            throw new Exception("No such value(BoatReservation service)");
        }
        return this.reservationRepository.findById(id).get();
    }

    @Override
    public List<BoatReservation> findByBoat(Long id) throws Exception {
        Boat boat = (Boat) this.boatService.findById(id);

        return this.reservationRepository.findByBoat(id);
    }

    @Override
    public List<BoatReservation> getAllOwnersReservations(Long id) throws Exception {
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();

        return this.reservationRepository.getAllOwnersReservations(id);
    }

    @Override
    public List<BoatReservation> getAllOwnersReservedReservations(Long id) throws Exception {
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();

        return this.reservationRepository.getAllOwnersReservedReservations(id);
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
    public List<BoatReservation> getAllOwnersNowAndUpcomingReservations(Long id) throws Exception {
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();
        List<BoatReservation> all = this.reservationRepository.getAllOwnersReservations(id);
        List<BoatReservation> upcoming = new ArrayList<>();

        for (BoatReservation res : all) {
            if( (res.getStartTime().isAfter(LocalDateTime.now()) || res.getStartTime().isBefore(LocalDateTime.now())
            || res.getStartTime().isEqual(LocalDateTime.now()))
                    &&  res.getEndTime().isAfter(LocalDateTime.now())){
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
            if((res.getStartTime().isBefore(LocalDateTime.now())) && (res.getEndTime().isBefore(LocalDateTime.now()))
            && (Objects.equals(res.getBoatOwner().getId(), boatOwner.getId()))) {
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
        br.setStartTime(boatReservation.getStartTime());
        br.setEndTime(boatReservation.getEndTime());
        br.setStartDate(boatReservation.getStartTime().toLocalDate());
        br.setEndDate(boatReservation.getEndTime().toLocalDate());
        br.setNumPersons(boatReservation.getNumPersons());
        br.setDiscountPrice(boatReservation.getDiscountPrice());
        br.setAdditionalServices(boatReservation.getAdditionalServices());
        br.setBoatOwner(boatReservation.getBoatOwner());
        br.setBoat(boatReservation.getBoat());
        br.setDiscount(true);
        br.setDeleted(false);
        br.setReserved(false);
        br.setClient(boatReservation.getClient());
        br.CalculatePrice();
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

    @Override
    public Set<BoatReservation> findByInterval(LocalDate startDate, LocalDate endDate, Long id) throws Exception{
        List<BoatReservation> reservations = this.getOwnersPastReservations(id);
        Set<BoatReservation> filtered = new HashSet<>();
        Double income = 0.0;

        for(BoatReservation res: reservations){
            if(res.getStartDate().isAfter(startDate) && res.getEndDate().isBefore(endDate)) {
              filtered.add(res);
            }
        }

        return filtered;
    }

    @Override
    public Set<BoatReservation> findByInterval2(LocalDate startDate, LocalDate endDate, Long id) throws Exception{
        List<BoatReservation> reservations = this.getOwnersPastReservations(id);
        Set<BoatReservation> filtered = new HashSet<>();
        Double attendance = 0.0;

        for(BoatReservation res: reservations){
            if(res.getStartDate().isAfter(startDate) && res.getEndDate().isBefore(endDate)) {
                filtered.add(res);
            }
        }

        return filtered;
    }

    @Override
    public void setDate(BoatReservation reservation) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate sd = LocalDate.parse(reservation.getStartDateString(), formatter);
        LocalDate ed = LocalDate.parse(reservation.getEndDateString(), formatter);

        reservation.setStartDate(sd);
        reservation.setEndDate(ed);
        reservation.setStartTime(sd.atStartOfDay());
        reservation.setEndTime(ed.atStartOfDay());
    }

    @Override
    public BoatReservation save(BoatReservation reservation) {
        return this.reservationRepository.save(reservation);
    }

    @Override
    public BoatReservation makeReservationWithClient(BoatReservation reservation, Boat boat, Long clid) throws Exception {
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();
        Client client = (Client) userService.findById(clid);

        reservation.setBoat(boat);
        reservation.setBoatOwner(boat.getBoatOwner());
        reservation.setClient(client);
        reservation.setPrice(boat.getPrice());
        reservation.CalculatePrice();
        reservation.setReserved(true);
        this.setDate(reservation);
        this.save(reservation);

        return reservation;
    }

}


