package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.*;
import com.isa.cottages.Repository.CottageReservationRepository;
import com.isa.cottages.Service.CottageReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CottageReservationServiceImpl implements CottageReservationService {

    @Autowired
    private CottageReservationRepository reservationRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CottageServiceImpl cottageService;

    @Autowired
    private ClientServiceImpl clientService;

//    @Autowired
//    public CottageReservationServiceImpl(CottageReservationRepository reservationRepository,
//                                         UserServiceImpl userService,
//                                         CottageServiceImpl cottageService,
//                                         ClientServiceImpl clientService) {
//        this.reservationRepository = reservationRepository;
//        this.userService = userService;
//        this.cottageService = cottageService;
//        this.clientService = clientService;
//    }

    @Override
    public CottageReservation findOne(Long id) {
        return reservationRepository.getOne(id);
    }

    @Override
    public List<CottageReservation> getAllOwnersReservations(Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();

        return this.reservationRepository.getAllOwnersReservations(id);
    }

    @Override
    public List<CottageReservation> getAllOwnersReservedReservations(Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();

        return this.reservationRepository.getAllOwnersReservedReservations(id);
    }

    @Override
    public List<CottageReservation> findByOrderByStartTimeAsc() throws Exception {
        List<CottageReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(CottageReservation::getStartTime));

        return pastOnes;
    }

    @Override
    public List<CottageReservation> findByOrderByStartTimeDesc() throws Exception {
        List<CottageReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(CottageReservation::getStartTime).reversed());

        return pastOnes;
    }

    @Override
    public List<CottageReservation> findByOrderByDurationAsc() throws Exception {
        List<CottageReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(CottageReservation::getDuration));

        return pastOnes;
    }

    @Override
    public List<CottageReservation> findByOrderByDurationDesc() throws Exception {
        List<CottageReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(CottageReservation::getDuration).reversed());

        return pastOnes;
    }

    @Override
    public List<CottageReservation> findByOrderByPriceAsc() throws Exception {
        List<CottageReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(CottageReservation::getPrice));

        return pastOnes;
    }

    @Override
    public List<CottageReservation> findByOrderByPriceDesc() throws Exception {
        List<CottageReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(CottageReservation::getPrice).reversed());

        return pastOnes;
    }

    @Override
    public List<CottageReservation> getOwnersUpcomingReservations(Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        List<CottageReservation> all = this.reservationRepository.getAllReservedByOwner(id);
        List<CottageReservation> upcoming = new ArrayList<>();

        for (CottageReservation res: all) {
            if((res.getStartTime().isAfter(LocalDateTime.now())) && (Objects.equals(res.getCottageOwner().getId(), cottageOwner.getId()))) {
                upcoming.add(res);
            }
        }
        return upcoming;
    }

    @Override
    public List<CottageReservation> getOwnersPastReservations(Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
//        List<Cottage> cottage = this.cottageService.findByCottageOwner(id);
        List<CottageReservation> all = this.reservationRepository.getAllReservedByOwner(id);
        List<CottageReservation> pastOnes = new ArrayList<>();

        for (CottageReservation res:all) {
            if((res.getStartTime().isBefore(LocalDateTime.now())) && (Objects.equals(res.getCottageOwner().getId(), cottageOwner.getId()))) {
                pastOnes.add(res);
            }
        }
        return pastOnes;
    }

    @Override
    public List<CottageReservation> getAllOwnersUpcomingReservations(Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        List<CottageReservation> all = this.reservationRepository.getAllOwnersReservations(id);
        List<CottageReservation> upcoming = new ArrayList<>();

        for (CottageReservation res: all) {
            if((res.getStartTime().isAfter(LocalDateTime.now())) && (Objects.equals(res.getCottageOwner().getId(), cottageOwner.getId()))) {
                upcoming.add(res);
            }
        }
        return upcoming;
    }

    @Override
    public CottageReservation saveDiscount(CottageReservation cottageReservation) {
        CottageReservation cr = new CottageReservation();

        cr.setDiscountAvailableFrom(cottageReservation.getDiscountAvailableFrom());
        cr.setDiscountAvailableUntil(cottageReservation.getDiscountAvailableUntil());
        cr.setNumPersons(cottageReservation.getNumPersons());
        cr.setDiscountPrice(cottageReservation.getDiscountPrice());
        cr.setAdditionalServices(cottageReservation.getAdditionalServices());
        cr.setCottageOwner(cottageReservation.getCottageOwner());
        cr.setCottage(cottageReservation.getCottage());
        cr.setDiscount(true);
        cr.setDeleted(false);
        cr.setReserved(false);
        cr.setClient(cottageReservation.getClient());
        this.reservationRepository.save(cr);

        return cr;
    }

    @Override
    public List<CottageReservation> findDiscountsByCottage(Long id) throws Exception{
        Cottage cottage = cottageService.findById(id);
        List<CottageReservation> all = this.reservationRepository.findDiscountsByCottage(id);
        List<CottageReservation> cr = new ArrayList<>();

        for (CottageReservation c:all) {
            if(Objects.equals(c.getCottage().getId(), cottage.getId())) {
                cr.add(c);
            }
        }
        return cr;
    }

    @Override
    public List<CottageReservation> getUpcomingReservations() throws Exception {
        Client cl = this.clientService.findByEmail(this.userService.getUserFromPrincipal().getEmail());
        List<CottageReservation> all = this.reservationRepository.getAllReserved();
        List<CottageReservation> upcoming = new ArrayList<>();

        for (CottageReservation res: all) {
            if((res.getStartTime().isAfter(LocalDateTime.now())) && (res.getEndTime().isAfter(LocalDateTime.now()))
                    && (Objects.equals(res.getCottageOwner().getId(), cl.getId()))) {
                upcoming.add(res);
            }
        }
        return upcoming;
    }

    @Override
    public List<CottageReservation> getPastReservations() throws Exception {
        Client cl = this.clientService.findByEmail(this.userService.getUserFromPrincipal().getEmail());
        List<CottageReservation> all = this.reservationRepository.getAllReserved();
        List<CottageReservation> pastOnes = new ArrayList<>();

        for (CottageReservation res: all) {
            if((res.getStartTime().isBefore(LocalDateTime.now())) && (res.getEndTime().isBefore(LocalDateTime.now())) &&
                    (Objects.equals(res.getClient().getId(), cl.getId()))) {
                pastOnes.add(res);
            }
        }
        return pastOnes;
    }

    @Override
    public List<CottageReservation> findAllByClient(Client client){
        return this.reservationRepository.findAllByClient(client.getId());
    }

    @Override
    public List<CottageReservation> findClient(String keyword) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();

        return this.reservationRepository.findClient(keyword);
    }

//    public List<CottageReservation> findPastOwnersReservationsByClient(Long id, String keyword) throws Exception {
//
//    }

    @Override
    public Set<CottageReservation> findByInterval(LocalDate startDate, LocalDate endDate, Long id) throws Exception{
        List<CottageReservation> reservations = this.getOwnersPastReservations(id);
        Set<CottageReservation> filtered = new HashSet<>();

        for(CottageReservation res: reservations){
            if(res.getStartDate().isAfter(startDate) && res.getEndDate().isBefore(endDate)) {
                filtered.add(res);
            }
        }
        return filtered;
    }

    @Override
    public void setDate(CottageReservation reservation) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate sd = LocalDate.parse(reservation.getStartDateString(), formatter);
        LocalDate ed = LocalDate.parse(reservation.getEndDateString(), formatter);

        reservation.setStartDate(sd);
        reservation.setEndDate(ed);
        reservation.setStartTime(sd.atStartOfDay());
        reservation.setEndTime(ed.atStartOfDay());
    }

    @Override
    public CottageReservation save(CottageReservation reservation) {
        return this.reservationRepository.save(reservation);
    }

    @Override
    public CottageReservation makeReservationWithClient(CottageReservation reservation, Cottage cottage, Long clid) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        Client client = (Client) userService.findById(clid);

        reservation.setCottage(cottage);
        reservation.setCottageOwner(cottage.getCottageOwner());
        reservation.setClient(client);
        reservation.setPrice(cottage.getPrice());
        reservation.CalculatePrice();
        reservation.setReserved(true);
        this.setDate(reservation);
        this.save(reservation);

        return reservation;
    }
}
