package com.isa.cottages.Service.impl;

import com.isa.cottages.Email.EmailService;
import com.isa.cottages.Model.*;
import com.isa.cottages.Repository.InstructorReservationRepository;
import com.isa.cottages.Service.InstructorReservationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class InstructorReservationsServiceImpl implements InstructorReservationsService {

    private ClientServiceImpl clientService;
    private UserServiceImpl userService;
    private InstructorReservationRepository reservationRepository;
    private EmailService emailService;

    @Autowired
    public InstructorReservationsServiceImpl(ClientServiceImpl clientService, UserServiceImpl userService,
                                             InstructorReservationRepository instructorReservationRepository,
                                             EmailService emailService) {
        this.clientService = clientService;
        this.userService = userService;
        this.reservationRepository = instructorReservationRepository;
        this.emailService = emailService;
    }

    @Override
    public List<InstructorReservation> getAllUpcoming() {
        List<InstructorReservation> all = this.reservationRepository.getAllReserved();
        List<InstructorReservation> upcoming = new ArrayList<>();

        for (InstructorReservation res: all) {
            if(res.getStartTime().isAfter(LocalDateTime.now()) && res.getEndTime().isAfter(LocalDateTime.now())) {
                upcoming.add(res);
            }
        }
        return upcoming;
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

    @Override
    public List<InstructorReservation> findByOrderByStartTimeAsc() throws Exception {
        List<InstructorReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(InstructorReservation::getStartTime));

        return pastOnes;
    }

    @Override
    public List<InstructorReservation> findByOrderByStartTimeDesc() throws Exception {
        List<InstructorReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(InstructorReservation::getStartTime).reversed());

        return pastOnes;
    }

    @Override
    public List<InstructorReservation> findByOrderByDurationAsc() throws Exception {
        List<InstructorReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(InstructorReservation::getDuration));

        return pastOnes;
    }

    @Override
    public List<InstructorReservation> findByOrderByDurationDesc() throws Exception {
        List<InstructorReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(InstructorReservation::getDuration).reversed());

        return pastOnes;
    }

    @Override
    public List<InstructorReservation> findByOrderByPriceAsc() throws Exception {
        List<InstructorReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(InstructorReservation::getPrice));

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
    public InstructorReservation makeReservation(InstructorReservation reservation, FishingInstructorAdventure instructor) throws Exception {
        Client client = (Client) this.userService.getUserFromPrincipal();

        reservation.setFishingInstructorAdventure(instructor);
        reservation.setClient(client);
        reservation.setPrice(instructor.getPrice());
        reservation.CalculatePrice();
        reservation.setReserved(true);
        this.setDate(reservation);
        this.save(reservation);

        this.sendReservationMail(reservation);

        return reservation;
    }

    @Override
    public InstructorReservation save(InstructorReservation instructorReservation) { return this.reservationRepository.save(instructorReservation); }

    @Override
    public void sendReservationMail(InstructorReservation reservation) {
        String to = reservation.getClient().getEmail();
        String topic = "Fishing instructor Reservation";
        String body = "You successfully made Fishing instructor reservation. \n\n\n" +
                "Instructor:\t" + reservation.getFishingInstructorAdventure().getInstructor().getFullName() + "\n" +
                "Instructor:\t" + reservation.getFishingInstructorAdventure().getInstructorInfo() + "\n" +
                "Adventure:\t" + reservation.getFishingInstructorAdventure().getAdventureName() + "\n" +
                "Start date\t" + reservation.getStartDate().toString() + "\n" +
                "End date\t" + reservation.getEndDate().toString() + "\n" +
                "Price:\t" + reservation.getPrice().toString() + "\n";

        this.emailService.sendEmail(to, body, topic);
    }

    @Override
    public List<InstructorReservation> findByOrderByPriceDesc() throws Exception {
        List<InstructorReservation> pastOnes = getPastReservations();
        pastOnes.sort(Comparator.comparing(InstructorReservation::getPrice).reversed());

        return pastOnes;
    }

    @Override
    public List<InstructorReservation> findAllByClient(Client client) { return this.reservationRepository.findAllByClient(client.getId()); }
}
