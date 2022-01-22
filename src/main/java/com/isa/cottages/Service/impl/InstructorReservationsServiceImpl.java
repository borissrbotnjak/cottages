package com.isa.cottages.Service.impl;

import com.isa.cottages.Email.EmailService;
import com.isa.cottages.Model.*;
import com.isa.cottages.Repository.InstructorReservationRepository;
import com.isa.cottages.Service.InstructorReservationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class InstructorReservationsServiceImpl implements InstructorReservationsService {

    private ClientServiceImpl clientService;
    private UserServiceImpl userService;
    private InstructorReservationRepository reservationRepository;
    private EmailService emailService;
    private LoyaltyProgramServiceImpl loyaltyService;

    @Autowired
    public InstructorReservationsServiceImpl(ClientServiceImpl clientService, UserServiceImpl userService,
                                             InstructorReservationRepository instructorReservationRepository,
                                             EmailService emailService, LoyaltyProgramServiceImpl loyaltyService) {
        this.clientService = clientService;
        this.userService = userService;
        this.reservationRepository = instructorReservationRepository;
        this.emailService = emailService;
        this.loyaltyService = loyaltyService;
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

        double discount = this.loyaltyService.calculateClientDiscount(client);
        if (discount > 0) { reservation.setPrice(instructor.getPrice() * (1.0-discount)); }
        else { reservation.setPrice(instructor.getPrice()); }

        this.loyaltyService.updateAfterReservation(client.getLoyaltyProgram());

        reservation.setReserved(true);
        this.setDate(reservation);
        reservation.calculateDuration(reservation.getStartDate(),
                reservation.getEndDate());

        Double price = this.CalculatePrice(reservation);
        reservation.setPrice(price);

        this.save(reservation);

        this.sendReservationMail(reservation);

        return reservation;
    }

    @Override
    public List<InstructorReservation> getAllWithDiscount(Long instructorId) {
        List<InstructorReservation> all = this.reservationRepository.findAllWithDiscount(instructorId);
        List<InstructorReservation> upcoming = new ArrayList<>();

        for (InstructorReservation res : all) {
            if (res.getStartTime().isAfter(LocalDateTime.now()) && (res.getEndTime().isAfter(LocalDateTime.now()))) {
                upcoming.add(res);
            }
        }
        return upcoming;
    }

    @Override
    public InstructorReservation makeReservationOnDiscount(Long id) throws Exception {
        Client client = (Client) this.userService.getUserFromPrincipal();
        InstructorReservation reservation = this.getOne(id);

        reservation.setClient(client);
        reservation.setReserved(true);
        reservation.setFishingInstructorAdventure(reservation.getFishingInstructorAdventure());
        Double price = this.CalculatePrice(reservation);
        reservation.setPrice(price);
        this.update(reservation);

        this.sendReservationMail(reservation);

        return reservation;
    }

    @Override
    public Double CalculatePrice(InstructorReservation reservation) throws ParseException {
        Double sum = reservation.getPrice();
        if (reservation.getDiscount() && reservation.getDiscountPrice() != 0.0) {
            sum = reservation.getDiscountPrice();
        }

        for (AdditionalService s : reservation.getAdditionalServices()) {
            sum += s.getPrice();
        }
        return sum * reservation.getDuration();
    }
/*
    @Override
    public Integer getDuration(InstructorReservation reservation) throws ParseException {
        SimpleDateFormat sdf  = new SimpleDateFormat("dd-MM-yyyy");
        Date sd = sdf.parse(String.valueOf(reservation.getStartDate()));
        Date ed = sdf.parse(String.valueOf(reservation.getEndDate()));

        long difference_In_Time = ed.getTime() - sd.getTime();
        long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24))  % 365;
        return Math.toIntExact(difference_In_Days);
    }
*/
    @Override
    public Boolean canCancel(Long id) {
        if (this.getOne(id).getStartTime().isAfter(LocalDateTime.now().plusDays(3))) {
            return true;
        }
        return false;
    }

    @Override
    public void cancel(Long id) {
        InstructorReservation reservation = this.getOne(id);

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
    public InstructorReservation update(InstructorReservation reservation) {
        InstructorReservation toUpdate = this.reservationRepository.getById(reservation.getId());

        toUpdate.setPrice(reservation.getPrice());
        toUpdate.setFishingInstructorAdventure(reservation.getFishingInstructorAdventure());
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
    public InstructorReservation getOne(Long id) {
        return this.reservationRepository.getById(id);
    }

    @Override
    public InstructorReservation save(InstructorReservation instructorReservation) { return this.reservationRepository.save(instructorReservation); }

    @Override
    public void sendReservationMail(InstructorReservation reservation) {
        String to = reservation.getClient().getEmail();
        String topic = "Fishing instructor Reservation";
        String body = "You successfully made Fishing instructor reservation. \n\n\n" +
                "\tInstructor:\t" + reservation.getFishingInstructorAdventure().getInstructor().getFullName() + "\n" +
                "\tAdventure:\t" + reservation.getFishingInstructorAdventure().getAdventureName() + "\n\n" +
                "\tStart date\t" + reservation.getStartDate().atStartOfDay().toLocalDate().toString() + "\n" +
                "\tEnd date\t" + reservation.getEndDate().atStartOfDay().toLocalDate().toString() + "\n\n" +
                "\tAddress:\t" + reservation.getFishingInstructorAdventure().getInstructor().getResidence() + ", " +
                reservation.getFishingInstructorAdventure().getInstructor().getState() + "\n" +
                "\tPrice:\t" + reservation.getPrice().toString() + "0  RSD\n";

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

    @Override
    public List<InstructorReservation> getAllUnavailable(LocalDate desiredStart, LocalDate desiredEnd) {
        return this.reservationRepository.findAllUnavailable(desiredStart, desiredEnd);
    }

    @Override
    public List<InstructorReservation> getAllAvailable(LocalDate desiredStart, LocalDate desiredEnd, int capacity) {
        return this.reservationRepository.findAllAvailable(desiredStart, desiredEnd, capacity);
    }
}
