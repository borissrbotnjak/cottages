package com.isa.cottages.Service.impl;

import com.isa.cottages.Email.EmailService;
import com.isa.cottages.Model.*;
import com.isa.cottages.Repository.FishingInstructorAdventureRepository;
import com.isa.cottages.Repository.FishingInstructorAdventureReservationRepository;
import com.isa.cottages.Service.FishingInstructorAdventureReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class FishingInstructorAdventureReservationServiceImpl implements FishingInstructorAdventureReservationService {
    private final FishingInstructorAdventureReservationRepository reservationRepository;
    private final FishingInstructorAdventureRepository adventureRepository;
    private final UserServiceImpl userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    public FishingInstructorAdventureReservationServiceImpl(FishingInstructorAdventureReservationRepository reservationRepository,
                                                            UserServiceImpl userService, FishingInstructorAdventureRepository adventureRepository) {
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.adventureRepository = adventureRepository;
    }

    @Override
    public List<AdventureReservation> getInstructorsUpcomingReservations(Long id) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        List<AdventureReservation> all = this.reservationRepository.getAllReservedByInstructor(id);
        List<AdventureReservation> upcoming = new ArrayList<>();

        for (AdventureReservation res : all)
            if ((res.getStartTime().isAfter(LocalDateTime.now())) && (Objects.equals(res.getInstructor().getId(), instructor.getId())))
                upcoming.add(res);
        return upcoming;
    }
    @Override
    public List<AdventureReservation> getAllMyAvailable(LocalDate desiredStart, LocalDate desiredEnd, int capacity, Long id) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        List<AdventureReservation> all = this.reservationRepository.findAllMyAvailable(desiredStart, desiredEnd, capacity, id);
        List<AdventureReservation> filtered = new ArrayList<>();

        for (AdventureReservation res : all) {
            if (Objects.equals(res.getInstructor().getId(), instructor.getId())) {
                filtered.add(res);
            }
        }
        return filtered;
    }
    @Override
    public List<AdventureReservation> getAllMyUnavailable(LocalDate desiredStart, LocalDate desiredEnd, Long id) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        List<AdventureReservation> all = this.reservationRepository.findAllMyUnavailable(desiredStart, desiredEnd, id);
        List<AdventureReservation> filtered = new ArrayList<>();

        for (AdventureReservation res:all) {
            if(Objects.equals(res.getInstructor().getId(), instructor.getId())) {
                filtered.add(res);
            }
        }
        return filtered;
    }
    @Override
    public void sendReservationMail(AdventureReservation reservation) {
        String to = reservation.getClient().getEmail();
        String topic = "Adventure Reservation";
        String body = "You successfully made cottage reservation. \n\n\n" +
                "\tAdventure:\t" + reservation.getAdventure().getAdventureName() + "\n" +
                "\tInstructor:\t" + reservation.getInstructor().getFullName() + "\n\n" +
                "\tStart date\t" + reservation.getStartDate().atStartOfDay().toLocalDate().toString() + "\n" +
                "\tEnd date\t" + reservation.getEndDate().atStartOfDay().toLocalDate().toString() + "\n\n" +
                "\tAddress:\t" + reservation.getInstructor().getResidence() + ", " +
                reservation.getInstructor().getState() + "\n" +
                "\tPrice:\t" + reservation.getPrice().toString() + "0  RSD\n";

        this.emailService.sendEmail(to, body, topic);
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
    public AdventureReservation update(AdventureReservation reservation) {
        AdventureReservation toUpdate = this.reservationRepository.getById(reservation.getId());

        toUpdate.setPrice(reservation.getPrice());
        toUpdate.setAdventure(reservation.getAdventure());
        toUpdate.setInstructor(reservation.getInstructor());
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
    public AdventureReservation getOne(Long id) {
        return this.reservationRepository.getById(id);
    }
    @Override
    public AdventureReservation makeReservationOnDiscount(Long reservationId) throws Exception {
        Client client = (Client) this.userService.getUserFromPrincipal();
        AdventureReservation reservation = this.getOne(reservationId);

        reservation.setClient(client);
        reservation.setReserved(true);
        reservation.setInstructor(reservation.getAdventure().getInstructor());
        reservation.CalculatePrice();
        this.update(reservation);

        this.sendReservationMail(reservation);

        return reservation;
    }

    @Override
    public Boolean canCancel(Long id) {
        return this.getOne(id).getStartTime().isAfter(LocalDateTime.now().plusDays(3));
    }

    @Override
    public void cancel(Long id) {
        AdventureReservation reservation = this.getOne(id);

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
    public List<AdventureReservation> getUpcomingReservations() throws Exception {
        Client cl = (Client) this.userService.getUserFromPrincipal();
        List<AdventureReservation> all = this.reservationRepository.getAllReserved();
        List<AdventureReservation> upcoming = new ArrayList<>();

        for (AdventureReservation res: all) {
            if((res.getStartTime().isAfter(LocalDateTime.now())) && (res.getEndTime().isAfter(LocalDateTime.now()))
                    && (Objects.equals(res.getClient().getId(), cl.getId()))) {
                upcoming.add(res);
            }
        }
        return upcoming;
    }

    @Override
    public List<AdventureReservation> getAllUpcoming() {

        List<AdventureReservation> all = this.reservationRepository.getAllReserved();
        List<AdventureReservation> upcoming = new ArrayList<>();

        for (AdventureReservation res: all) {
            if(res.getStartTime().isAfter(LocalDateTime.now()) && res.getEndTime().isAfter(LocalDateTime.now())) {
                upcoming.add(res);
            }
        }
        return upcoming;
    }

    @Override
    public List<AdventureReservation> getPastReservations() throws Exception {
        Client cl = (Client) this.userService.getUserFromPrincipal();
        List<AdventureReservation> all = this.reservationRepository.getAllReserved();
        List<AdventureReservation> pastOnes = new ArrayList<>();

        for (AdventureReservation res: all) {
            if((res.getStartTime().isBefore(LocalDateTime.now())) && (res.getEndTime().isBefore(LocalDateTime.now())) &&
                    (Objects.equals(res.getClient().getId(), cl.getId()))) {
                pastOnes.add(res);
            }
        }
        return pastOnes;
    }

    @Override
    public List<AdventureReservation> findClient(String keyword) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();

        return this.reservationRepository.findClient(keyword);
    }

    @Override
    public List<AdventureReservation> getInstructorsPastReservations(Long id) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        List<AdventureReservation> all = this.reservationRepository.getAllReservedByInstructor(id);
        List<AdventureReservation> pastOnes = new ArrayList<>();

        for (AdventureReservation res:all) {
            if((res.getStartTime().isBefore(LocalDateTime.now())) && (Objects.equals(res.getInstructor().getId(), instructor.getId()))) {
                pastOnes.add(res);
            }
        }
        return pastOnes;
    }

    @Override
    public Set<AdventureReservation> findByInterval(LocalDate startDate, LocalDate endDate, Long id) throws Exception{
        List<AdventureReservation> reservations = this.getInstructorsPastReservations(id);
        Set<AdventureReservation> filtered = new HashSet<>();

        for(AdventureReservation res: reservations){
            if(res.getStartDate().isAfter(startDate) && res.getEndDate().isBefore(endDate)) {
                filtered.add(res);
            }
        }
        return filtered;
    }

    @Override
    public Set<AdventureReservation> findByInterval2(LocalDate startDate, LocalDate endDate, Long id) throws Exception{
        List<AdventureReservation> reservations = this.getInstructorsPastReservations(id);
        Set<AdventureReservation> filtered = new HashSet<>();
        Double attendance = 0.0;

        for(AdventureReservation res: reservations){
            if(res.getStartDate().isAfter(startDate) && res.getEndDate().isBefore(endDate)) {
                filtered.add(res);
            }
        }

        return filtered;
    }

    @Override
    public List<AdventureReservation> getAllInstructorsNowAndUpcomingReservations(Long id) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        List<AdventureReservation> all = this.reservationRepository.getAllInstructorsReservations(id);
        List<AdventureReservation> upcoming = new ArrayList<>();

        for (AdventureReservation res : all) {
            if( (res.getStartTime().isAfter(LocalDateTime.now()) || res.getStartTime().isBefore(LocalDateTime.now())
                    || res.getStartTime().isEqual(LocalDateTime.now()))
                    &&  res.getEndTime().isAfter(LocalDateTime.now())) {
                upcoming.add(res);
            }
        }

        return upcoming;
    }

    @Override
    public List<AdventureReservation> findDiscountsByAdventure(Long id) {
        FishingInstructorAdventure adventure = adventureRepository.findById(id).get();
        List<AdventureReservation> all = this.reservationRepository.findDiscountsByAdventure(id);
        List<AdventureReservation> ar = new ArrayList<>();

        for (AdventureReservation a:all) {
            if(Objects.equals(a.getAdventure().getId(), adventure.getId())) {
                ar.add(a);
            }
        }
        return ar;
    }

    @Override
    public AdventureReservation saveDiscount(AdventureReservation adventureReservation) {
        AdventureReservation ar = new AdventureReservation();

        ar.setDiscountAvailableFrom(adventureReservation.getDiscountAvailableFrom());
        ar.setDiscountAvailableUntil(adventureReservation.getDiscountAvailableUntil());
        ar.setStartTime(adventureReservation.getStartTime());
        ar.setEndTime(adventureReservation.getEndTime());
        ar.setStartDate(adventureReservation.getStartTime().toLocalDate());
        ar.setEndDate(adventureReservation.getEndTime().toLocalDate());
        ar.setNumPersons(adventureReservation.getNumPersons());
        ar.setPrice(adventureReservation.getPrice());
        ar.setDiscountPrice(adventureReservation.getDiscountPrice());
        ar.setAdditionalServices(adventureReservation.getAdditionalServices());
        ar.setInstructor(adventureReservation.getInstructor());
        ar.setAdventure(adventureReservation.getAdventure());
        ar.setDiscount(true);
        ar.setDeleted(false);
        ar.setReserved(false);
        ar.setClient(adventureReservation.getClient());
        ar.CalculatePrice();
        this.reservationRepository.save(ar);

        return ar;
    }
    @Override
    public void setDate(AdventureReservation reservation) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate sd = LocalDate.parse(reservation.getStartDateString(), formatter);
        LocalDate ed = LocalDate.parse(reservation.getEndDateString(), formatter);

        reservation.setStartDate(sd);
        reservation.setEndDate(ed);
        reservation.setStartTime(sd.atStartOfDay());
        reservation.setEndTime(ed.atStartOfDay());
    }

    @Override
    public AdventureReservation save(AdventureReservation reservation) {
        return this.reservationRepository.save(reservation);
    }

    @Override
    public List<AdventureReservation> getAllWithDiscount(Long AdventureId) {
        List<AdventureReservation> all = this.reservationRepository.findAllWithDiscount(AdventureId);
        List<AdventureReservation> upcoming = new ArrayList<>();

        for (AdventureReservation res : all) {
            if (res.getStartTime().isAfter(LocalDateTime.now()) && (res.getEndTime().isAfter(LocalDateTime.now()))) {
                upcoming.add(res);
            }
        }
        return upcoming;
    }

    @Override
    public AdventureReservation makeReservationWithClient(AdventureReservation reservation, FishingInstructorAdventure adventure, Long clid) throws Exception {

        Client client = (Client) userService.findById(clid);

        reservation.setAdventure(adventure);
        reservation.setInstructor(adventure.getInstructor());
        reservation.setClient(client);
        reservation.setPrice(adventure.getPrice());
        reservation.CalculatePrice();
        reservation.setReserved(true);
        this.setDate(reservation);
        this.save(reservation);

        return reservation;
    }
}
