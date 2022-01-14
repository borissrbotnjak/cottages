package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.FishingInstructorAdventure;
import com.isa.cottages.Model.InstructorReservation;
import com.isa.cottages.Repository.FishingInstructorAdventureRepository;
import com.isa.cottages.Service.FishingInstructorAdventureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FishingInstructorAdventureServiceImpl implements FishingInstructorAdventureService {

    private final FishingInstructorAdventureRepository adventureRepository;
    private final InstructorReservationsServiceImpl reservationService;

    @Autowired
    public FishingInstructorAdventureServiceImpl(FishingInstructorAdventureRepository adventureRepository, InstructorReservationsServiceImpl reservationService) {
        this.adventureRepository = adventureRepository;
        this.reservationService = reservationService;
    }

    @Override
    public FishingInstructorAdventure findById(Long id) throws Exception {
        if (this.adventureRepository.findById(id).isEmpty()) {
            throw new Exception("No such value(adventure)");
        }
        return this.adventureRepository.findById(id).get();
    }

    @Override
    public List<FishingInstructorAdventure> findAll() {
        return this.adventureRepository.findAll();
    }

    @Override
    public List<FishingInstructorAdventure> findByKeyword(String keyword) { return this.adventureRepository.findByKeyword(keyword); }

    @Override
    public Boolean InstructorAvailable(LocalDate startDate, LocalDate endDate, FishingInstructorAdventure instructor, int numPersons) {
        if (instructor.getNumPersons() >= numPersons) {
            if (instructor.getAvailableFrom() != null && instructor.getAvailableUntil() != null) {
                if (instructor.getAvailableFrom().toLocalDate().isBefore(startDate) && instructor.getAvailableUntil().toLocalDate().isAfter(endDate)) { return true; }
            } else { return true; }
        }

        return false;
    }

    @Override
    public Set<FishingInstructorAdventure> findAllAvailable(LocalDate startDate, LocalDate endDate, int numOfPersons) throws Exception {

        Set<FishingInstructorAdventure> available = new HashSet<>();
        Set<FishingInstructorAdventure> withReservation = new HashSet<>();
        List<InstructorReservation> reservations = this.reservationService.getAllUpcoming();

        for (InstructorReservation res : reservations) {
            withReservation.add(res.getFishingInstructorAdventure());
            if (this.InstructorAvailable(startDate, endDate, res.getFishingInstructorAdventure(), numOfPersons)) {
                if ((res.getFishingInstructorAdventure().getNumPersons() >= numOfPersons && res.getStartDate().isAfter(endDate) && res.getEndDate().isAfter(endDate)) ||
                        (res.getFishingInstructorAdventure().getNumPersons() >= numOfPersons && res.getStartDate() == null && res.getEndDate() == null) ||
                        (res.getFishingInstructorAdventure().getNumPersons() >= numOfPersons && res.getStartDate().isBefore(startDate) && res.getEndDate().isBefore(startDate))) {
                    available.add(res.getFishingInstructorAdventure());
                }
            }
        }

        List<FishingInstructorAdventure> all = this.adventureRepository.findAll();

        HashSet<FishingInstructorAdventure> allSet = new HashSet<>(all);

        HashSet<FishingInstructorAdventure> woReservation = new HashSet<>(allSet) {{ removeAll(withReservation); }};

        for (FishingInstructorAdventure b : woReservation) {
            if (b.getNumPersons() >= numOfPersons) {  available.add(b); }
        }

        return available;
    }

    @Override
    public List<FishingInstructorAdventure> findAllAvailableSorted(LocalDate startDate, LocalDate endDate, int numOfPersons, Boolean asc, Boolean price, Boolean rating) throws Exception {
        Set<FishingInstructorAdventure> set = this.findAllAvailable(startDate, endDate, numOfPersons);
        List<FishingInstructorAdventure> available = new ArrayList<>(set);

        if (asc && price && !rating) {
            available.sort(Comparator.comparing(FishingInstructorAdventure::getPrice));
        }
        else if (asc && !price && rating) {
            available.sort(Comparator.comparing(FishingInstructorAdventure::getAverageRating));
        }
        else if (!asc && price && !rating) {
            available.sort(Comparator.comparing(FishingInstructorAdventure::getPrice).reversed());
        }
        else if (!asc && !price && rating) {
            available.sort(Comparator.comparing(FishingInstructorAdventure::getAverageRating).reversed());
        }

        return available;
    }

    @Override
    public FishingInstructorAdventure saveAdventure(FishingInstructorAdventure fishingInstructorAdventure) {
        FishingInstructorAdventure fia = new FishingInstructorAdventure();

        fia.setAdventureName(fishingInstructorAdventure.getAdventureName());
        fia.setAdventureCity(fishingInstructorAdventure.getAdventureCity());
        fia.setAdventureState(fishingInstructorAdventure.getAdventureState());
        fia.setAdventureResidence(fishingInstructorAdventure.getAdventureResidence());

        fia.setAdventureDescription(fishingInstructorAdventure.getAdventureDescription());
        fia.setAverageRating(fishingInstructorAdventure.getAverageRating());

        this.adventureRepository.save(fia);
        return fia;
    }

    @Override
    public List<FishingInstructorAdventure> findByOrderByAdventureNameAsc() { return this.adventureRepository.findByOrderByAdventureNameAsc(); }

    @Override
    public List<FishingInstructorAdventure> findByOrderByAdventureNameDesc() { return this.adventureRepository.findByOrderByAdventureNameDesc(); }

    @Override
    public List<FishingInstructorAdventure> findByOrderByRatingAsc() { return this.adventureRepository.findByOrderByAverageRatingAsc(); }

    @Override
    public List<FishingInstructorAdventure> findByOrderByRatingDesc() { return this.adventureRepository.findByOrderByAverageRatingDesc(); }

    @Override
    public List<FishingInstructorAdventure> findByOrderByAddressAsc() { return this.adventureRepository.findByOrderByAdventureResidenceAscAdventureCityAscAdventureStateAsc(); }

    @Override
    public List<FishingInstructorAdventure> findByOrderByAddressDesc() { return this.adventureRepository.findByOrderByAdventureResidenceDescAdventureCityDescAdventureStateDesc(); }

    @Override
    public List<FishingInstructorAdventure> sortByInstructorInfo(Boolean asc) {
        if (asc) {
            return this.adventureRepository.findByOrderByInstructorInfoAsc();
        } else {
            return this.adventureRepository.findByOrderByInstructorInfoDesc();
        }
    }
}
