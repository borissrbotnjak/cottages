package com.isa.cottages.Service.impl;

import com.isa.cottages.Email.EmailService;
import com.isa.cottages.Model.*;
import com.isa.cottages.Repository.CottageRepository;
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

    private CottageReservationRepository reservationRepository;
    private CottageRepository cottageRepository;
    private UserServiceImpl userService;
    // private CottageServiceImpl cottageService;
    private ClientServiceImpl clientService;
    private EmailService emailService;

    @Autowired
    public CottageReservationServiceImpl(CottageReservationRepository reservationRepository,
                                         UserServiceImpl userService,
                                         CottageRepository cottageRepository,
                                         ClientServiceImpl clientService,
                                         EmailService emailService) {
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.cottageRepository = cottageRepository;
        this.clientService = clientService;
        this.emailService = emailService;
    }

    @Override
    public List<CottageReservation> getAllOwnersReservations(Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();

        return this.reservationRepository.getAllReservedByOwner(id);
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
    public void setDate(Reservation reservation) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate sd = LocalDate.parse(reservation.getStartDateString(), formatter);
        LocalDate ed = LocalDate.parse(reservation.getEndDateString(), formatter);

        reservation.setStartDate(sd);
        reservation.setEndDate(ed);
        reservation.setStartTime(sd.atStartOfDay());
        reservation.setEndTime(ed.atStartOfDay());
    }

    @Override
    public CottageReservation makeReservation(CottageReservation reservation, Cottage cottage) throws Exception {
        Client client = (Client) this.userService.getUserFromPrincipal();

        reservation.setCottage(cottage);
        reservation.setCottageOwner(cottage.getCottageOwner());
        reservation.setClient(client);
        reservation.setPrice(cottage.getPrice());
        reservation.CalculatePrice();
        reservation.setReserved(true);
        this.setDate(reservation);
        this.save(reservation);

        this.sendReservationMail(reservation);

        return reservation;
    }

    @Override
    public CottageReservation save(CottageReservation cottageReservation) { return this.reservationRepository.save(cottageReservation); }

    @Override
    public void sendReservationMail(CottageReservation reservation) {
        String to = reservation.getClient().getEmail();
        String topic = "Cottage Reservation";
        String body = "You successfully made cottage reservation. \n\n\n" +
                "\tCottage:\t" + reservation.getCottage().getName() + "\n" +
                "\tCottage Owner:\t" + reservation.getCottageOwner().getFullName() + "\n\n" +
                "\tStart date\t" + reservation.getStartDate().atStartOfDay().toLocalDate().toString() + "\n" +
                "\tEnd date\t" + reservation.getEndDate().atStartOfDay().toLocalDate().toString() + "\n\n" +
                "\tAddress:\t" + reservation.getCottageOwner().getResidence() + ", " +
                reservation.getCottageOwner().getState() + "\n" +
                "\tPrice:\t" + reservation.getPrice().toString() + "0  RSD\n";

        this.emailService.sendEmail(to, body, topic);
    }

    @Override
    public List<CottageReservation> getAllWithDiscount(Long CottageId) {
        List<CottageReservation> all = this.reservationRepository.findAllWithDiscount(CottageId);
        List<CottageReservation> upcoming = new ArrayList<>();

        for (CottageReservation res : all) {
            if (res.getStartTime().isAfter(LocalDateTime.now()) && (res.getEndTime().isAfter(LocalDateTime.now()))) {
                upcoming.add(res);
            }
        }
        return upcoming;
    }

    @Override
    public CottageReservation update(CottageReservation reservation) {
        CottageReservation toUpdate = this.reservationRepository.getById(reservation.getId());

        toUpdate.setPrice(reservation.getPrice());
        toUpdate.setCottage(reservation.getCottage());
        toUpdate.setCottageOwner(reservation.getCottageOwner());
        toUpdate.setReserved(reservation.getReserved());
        toUpdate.setStartTime(reservation.getStartTime());
        toUpdate.setEndTime(reservation.getEndTime());
        toUpdate.setClient(reservation.getClient());
        toUpdate.setNumPersons(reservation.getNumPersons());
        toUpdate.setStartDate(reservation.getStartDate());
        toUpdate.setEndDate(reservation.getEndDate());
        toUpdate.setDuration(reservation.getDuration());
        toUpdate.setAdditionalServices(reservation.getAdditionalServices());
        toUpdate.setDiscountPrice(reservation.getDiscountPrice());
        toUpdate.setDiscountAvailableFrom(reservation.getDiscountAvailableFrom());
        toUpdate.setDiscountAvailableUntil(reservation.getDiscountAvailableUntil());

        this.reservationRepository.save(toUpdate);
        return toUpdate;
    }

    @Override
    public CottageReservation getOne(Long id) {
        return this.reservationRepository.getById(id);
    }

    @Override
    public CottageReservation makeReservationOnDiscount(Long reservationId) throws Exception {
        Client client = (Client) this.userService.getUserFromPrincipal();
        CottageReservation reservation = this.getOne(reservationId);

        reservation.setClient(client);
        reservation.setReserved(true);
        reservation.setCottageOwner(reservation.getCottage().getCottageOwner());
        reservation.CalculatePrice();
        this.update(reservation);

        this.sendReservationMail(reservation);

        return reservation;
    }

    @Override
    public Boolean canCancel(Long id) {
        if (this.getOne(id).getStartTime().isAfter(LocalDateTime.now().plusDays(3))) {
            return true;
        }
        return false;
    }

    @Override
    public void cancel(Long id) {
        CottageReservation reservation = this.getOne(id);

        if(reservation.getDiscount()) {
            reservation.setClient(null);
            reservation.setReserved(false);
            this.update(reservation);
        }
        else {
            reservation.setDeleted(true);
            this.update(reservation);
        }
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
    public CottageReservation saveDiscount(CottageReservation cottageReservation) {
        CottageReservation cr = new CottageReservation();

        cr.setDiscountAvailableFrom(cottageReservation.getDiscountAvailableFrom());
        cr.setDiscountAvailableUntil(cottageReservation.getDiscountAvailableUntil());
        cr.setNumPersons(cottageReservation.getNumPersons());
        cr.setPrice(cottageReservation.getPrice());
        cr.setAdditionalServices(cottageReservation.getAdditionalServices());
        cr.setCottageOwner(cottageReservation.getCottageOwner());
        cr.setCottage(cottageReservation.getCottage());
        cr.setDiscount(true);
        this.reservationRepository.save(cr);

        return cr;
    }

    @Override
    public List<CottageReservation> findDiscountsByCottage(Long id) {
        Cottage cottage = cottageRepository.findById(id).get();
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
                    && (Objects.equals(res.getClient().getId(), cl.getId()))) {
                upcoming.add(res);
            }
        }
        return upcoming;
    }

    @Override
    public List<CottageReservation> getAllUpcoming() {

        List<CottageReservation> all = this.reservationRepository.getAllReserved();
        List<CottageReservation> upcoming = new ArrayList<>();

        for (CottageReservation res: all) {
            if(res.getStartTime().isAfter(LocalDateTime.now()) && res.getEndTime().isAfter(LocalDateTime.now())) {
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
}
