package com.isa.cottages.Service.impl;

import com.isa.cottages.Email.EmailService;
import com.isa.cottages.Model.Boat;
import com.isa.cottages.Model.BoatReservation;
import com.isa.cottages.Model.Client;
import com.isa.cottages.Repository.BoatReservationRepository;
import com.isa.cottages.Service.BoatReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class BoatReservationServiceImpl implements BoatReservationService {

    private ClientServiceImpl clientService;
    private UserServiceImpl userService;
    private BoatReservationRepository reservationRepository;
    private EmailService emailService;

    @Autowired
    public BoatReservationServiceImpl(ClientServiceImpl clientService, UserServiceImpl userService,
                                      BoatReservationRepository boatReservationRepository, EmailService emailService) {
        this.clientService = clientService;
        this.userService = userService;
        this.reservationRepository = boatReservationRepository;
        this.emailService = emailService;
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
        // TODO: Check for availability period
        for (BoatReservation res : all) {
            if ((res.getStartTime().isAfter(LocalDateTime.now())) && (res.getEndTime().isAfter(LocalDateTime.now()))) {
                upcoming.add(res);
            }
        }
        return upcoming;
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
    public BoatReservation save(BoatReservation reservation) {
        return this.reservationRepository.save(reservation);
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

    public double getDiscountPrice(Double price) throws Exception {
        return (1 - this.clientService.getDiscount()) * price;
    }

    @Override
    public BoatReservation makeReservation(BoatReservation reservation, Boat boat) throws Exception {
        Client client = (Client) this.userService.getUserFromPrincipal();

        reservation.setBoat(boat);
        reservation.setBoatOwner(boat.getBoatOwner());
        reservation.setClient(client);
        reservation.setPrice(boat.getPrice());
        reservation.CalculatePrice();
        reservation.setReserved(true);
        this.setDate(reservation);
        this.save(reservation);

        this.sendReservationMail(reservation);

        return reservation;
    }

    @Override
    public void sendReservationMail(BoatReservation reservation) {
        String to = reservation.getClient().getEmail();
        String topic = "Boat Reservation";
        String body = "You successfully made boat reservation. \n\n\n" +
                "\tBoat:\t" + reservation.getBoat().getBoatName() + "\n" +
                "\tBoat Owner:\t" + reservation.getBoatOwner().getFullName() + "\n\n" +
                "\tStart date\t" + reservation.getStartDate().atStartOfDay().toLocalDate().toString() + "\n" +
                "\tEnd date\t" + reservation.getEndDate().atStartOfDay().toLocalDate().toString() + "\n\n" +
                "\tAddress:\t" + reservation.getBoatOwner().getResidence() + ", " +
                reservation.getBoatOwner().getState() + "\n" +
                "\tPrice:\t" + reservation.getPrice().toString() + "0  RSD\n";

        this.emailService.sendEmail(to, body, topic);
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
