package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.AdditionalService;
import com.isa.cottages.Model.AdventureReservation;
import com.isa.cottages.Model.FishingInstructorAdventure;
import com.isa.cottages.Model.Instructor;
import com.isa.cottages.Repository.AdditionalServiceRepository;
import com.isa.cottages.Repository.FishingInstructorAdventureRepository;
import com.isa.cottages.Service.FishingInstructorAdventureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class FishingInstructorAdventureServiceImpl implements FishingInstructorAdventureService {

    private final FishingInstructorAdventureRepository adventureRepository;
    private final AdditionalServiceRepository serviceRepository;
    private final InstructorServiceImpl instructorService;
    private final UserServiceImpl userService;
    private final FishingInstructorAdventureReservationServiceImpl reservationService;

    @Autowired
    public FishingInstructorAdventureServiceImpl(FishingInstructorAdventureRepository adventureRepository, AdditionalServiceRepository serviceRepository, InstructorServiceImpl instructorService, UserServiceImpl userService, FishingInstructorAdventureReservationServiceImpl reservationService) {
        this.adventureRepository = adventureRepository;
        this.userService = userService;
        this.instructorService=instructorService;
        this.serviceRepository = serviceRepository;
        this.reservationService = reservationService;
    }

    @Override
    public FishingInstructorAdventure findById(Long id) throws Exception {
        Optional<FishingInstructorAdventure> adventure = this.adventureRepository.findById(id);
        if (adventure.isEmpty()) {
            throw new Exception("No such value(adventure)");
        }
        return this.adventureRepository.findById(id).get();
    }

    @Override
    public List<FishingInstructorAdventure> findAll() {
        return this.adventureRepository.findAll();
    }

    @Override
    public Boolean canUpdateOrDelete(Long id) throws Exception {
        FishingInstructorAdventure adventure = findById(id);

        return !adventure.getReserved();
    }

    @Override
    public FishingInstructorAdventure defineAvailability(FishingInstructorAdventure adventure) throws Exception
    {
        FishingInstructorAdventure forUpdate = findById(adventure.getId());

        forUpdate.setAvailableFrom(adventure.getAvailableFrom());
        forUpdate.setAvailableUntil(adventure.getAvailableUntil());

        this.adventureRepository.save(forUpdate);
        return forUpdate;
    }

    @Override
    public List<FishingInstructorAdventure> findByKeyword(String keyword) {
        return this.adventureRepository.findByKeyword(keyword);
    }

    @Override
    public List<AdditionalService> findServicesByAdventure(FishingInstructorAdventure adventure) {
        return this.serviceRepository.findAllByAdventure(adventure);
    }

    @Override
    public AdditionalService saveService(AdditionalService additionalService) {
        return this.serviceRepository.save(additionalService);
    }


    @Override
    public Set<FishingInstructorAdventure> findAllAvailable(LocalDate startDate, LocalDate endDate, int numOfPersons) throws Exception {

        Set<FishingInstructorAdventure> available = new HashSet<>();
        Set<FishingInstructorAdventure> unAvailable = new HashSet<>();
        Set<FishingInstructorAdventure> withReservation = new HashSet<>();
        List<AdventureReservation> reservations = this.reservationService.getAllUpcoming();

        for (AdventureReservation res : reservations) {
            withReservation.add(res.getAdventure());

            if (!available.contains(res.getAdventure())) {
                if (this.adventureAvailable(startDate, endDate, res.getAdventure(), numOfPersons)) {
                    if ((res.getStartTime().toLocalDate().isAfter(endDate) && res.getEndTime().toLocalDate().isAfter(endDate)) ||
                            (res.getStartTime().toLocalDate().isBefore(startDate) && res.getEndTime().toLocalDate().isBefore(startDate))) {
                        available.add(res.getAdventure());
                    }
                } else { unAvailable.add(res.getAdventure()); }
            }
        }

        List<FishingInstructorAdventure> all = this.adventureRepository.findAll();
        HashSet<FishingInstructorAdventure> allSet = new HashSet<>(all);

        HashSet<FishingInstructorAdventure> woReservation = new HashSet<>(allSet) {{ removeAll(withReservation); }};

        for (FishingInstructorAdventure b : woReservation) {
            if (!unAvailable.contains(b) && this.adventureAvailable(startDate, endDate, b, numOfPersons)) { available.add(b); }
        }

        available.removeIf(unAvailable::contains);

        return available;
    }
    @Override
    public void updateAdventure(FishingInstructorAdventure adventure) throws Exception {
        FishingInstructorAdventure forUpdate = findById(adventure.getId());

        forUpdate.setAdventureName(adventure.getAdventureName());
        forUpdate.setAdventureResidence(adventure.getAdventureResidence());
        forUpdate.setAdventureCity(adventure.getAdventureCity());
        forUpdate.setAdventureState(adventure.getAdventureState());
        forUpdate.setAdventureDescription(adventure.getAdventureDescription());
        forUpdate.setMaxClients(adventure.getMaxClients());
        forUpdate.setImageUrl(adventure.getImageUrl());
        forUpdate.setAdditionalServices(adventure.getAdditionalServices());
        forUpdate.setConductRules(adventure.getConductRules());
        forUpdate.setAverageRating(adventure.getAverageRating());
        forUpdate.setRatings(adventure.getRatings());
        forUpdate.setReserved(adventure.getReserved());
        forUpdate.setGearIncluded(adventure.getGearIncluded());
        forUpdate.setPrice(adventure.getPrice());
        forUpdate.setCancellationFeePercent(adventure.getCancellationFeePercent());
        forUpdate.setInstructorInfo(adventure.getInstructorInfo());
        forUpdate.setAvailableFrom(adventure.getAvailableFrom());
        forUpdate.setAvailableUntil(adventure.getAvailableUntil());

        this.adventureRepository.save(forUpdate);
    }

    @Override
    public Boolean adventureAvailable(LocalDate startDate, LocalDate endDate, FishingInstructorAdventure adventure, int numPersons) {
        if (adventure.getMaxClients() >= numPersons) {
            if (adventure.getAvailableFrom() != null && adventure.getAvailableUntil() != null) {
                return adventure.getAvailableFrom().toLocalDate().isBefore(startDate) && adventure.getAvailableUntil().toLocalDate().isAfter(endDate);
            } else { return true; }
        }

        return false;
    }

    @Override
    public Boolean myAdventureAvailable(LocalDate startDate, LocalDate endDate, FishingInstructorAdventure adventure, Long id) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        if (!adventure.getDeleted()) {
            return (adventure.getAvailableFrom() == null && adventure.getAvailableUntil() == null) ||
                    adventure.getAvailableFrom().toLocalDate().isBefore(startDate) && adventure.getAvailableUntil().toLocalDate().isAfter(endDate)
                            && Objects.equals(adventure.getInstructor().getId(), instructor.getId());
        }
        return false;
    }
    @Override
    public Set<FishingInstructorAdventure> findAllMyAvailable(LocalDate startDate, LocalDate endDate, int numOfPersons, Long id) throws Exception {
        Set<FishingInstructorAdventure> available = new HashSet<>();
        Set<FishingInstructorAdventure> unavailable = new HashSet<>();

        List<AdventureReservation> reservations = this.reservationService.getAllMyAvailable(startDate, endDate, numOfPersons, id);
        for (AdventureReservation res : reservations) {
            if (isByInstructor(id, res.getAdventure()) && myAdventureAvailable(startDate, endDate, res.getAdventure(), id)) {
                available.add(res.getAdventure());
            }
        }

        List<AdventureReservation> un = this.reservationService.getAllMyUnavailable(startDate, endDate, id);
        for (AdventureReservation r : un) {
            if (isByInstructor(id, r.getAdventure())) {
                unavailable.add(r.getAdventure());
            }
        }

        // ako ne postoji rezervacija i dobar je kapacitet, dodaj
        List<FishingInstructorAdventure> all = this.adventureRepository.findByInstructor(id);
        HashSet<FishingInstructorAdventure> allSet = new HashSet<>(all);

        HashSet<FishingInstructorAdventure> woReservation = new HashSet<>(allSet) {{
            removeAll(available);
        }};

        for (FishingInstructorAdventure a : woReservation) {
//          boolean u = unavailable.contains(b);
            if (a.getMaxClients() >= numOfPersons && !unavailable.contains(a)
                    && this.myAdventureAvailable(startDate, endDate, a, id)){
                available.add(a);
            }
        }
        available.removeIf(unavailable::contains);

        return available;
    }
    @Override
    public Boolean isByInstructor(Long id, FishingInstructorAdventure adventure) throws Exception {
        Instructor instructor = (Instructor) userService.getUserFromPrincipal();

        if(Objects.equals(adventure.getInstructor().getId(), instructor.getId())) {
            return true;
        }
        return false;
    }
    @Override
    public List<FishingInstructorAdventure> findAllMyAvailableSorted(Long oid, LocalDate startDate, LocalDate endDate, int numOfPersons,
                                                  Boolean asc, Boolean price, Boolean rating) throws Exception {
        Instructor instructor = (Instructor) userService.getUserFromPrincipal();
        Set<FishingInstructorAdventure> set = this.findAllMyAvailable(startDate, endDate, numOfPersons, oid);
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
    public void removeAdventure(FishingInstructorAdventure adventure)throws Exception{

        Instructor instructor = adventure.getInstructor();
        Set<FishingInstructorAdventure> adventures = instructor.getAdventures();
        adventures.remove(adventure);
        instructor.setAdventures(adventures);
        adventure.setDeleted(true);
        adventure.setInstructor(null);
        this.instructorService.updateAdventures(instructor);
    }

    @Override
    public void saveAdventure(FishingInstructorAdventure fishingInstructorAdventure) {
        FishingInstructorAdventure fia = new FishingInstructorAdventure();

        fia.setAdventureName(fishingInstructorAdventure.getAdventureName());
        fia.setAdventureCity(fishingInstructorAdventure.getAdventureCity());
        fia.setAdventureState(fishingInstructorAdventure.getAdventureState());
        fia.setAdventureResidence(fishingInstructorAdventure.getAdventureResidence());
        fia.setInstructor(fishingInstructorAdventure.getInstructor());
        fia.setGearIncluded(fishingInstructorAdventure.getGearIncluded());
        fia.setInstructorInfo(fishingInstructorAdventure.getInstructorInfo());
        fia.setPrice(fishingInstructorAdventure.getPrice());
        fia.setReserved(fishingInstructorAdventure.getReserved());
        fia.setImageUrl(fishingInstructorAdventure.getImageUrl());
        fia.setConductRules(fishingInstructorAdventure.getConductRules());
        fia.setMaxClients(fishingInstructorAdventure.getMaxClients());
        fia.setAdventureDescription(fishingInstructorAdventure.getAdventureDescription());
        fia.setAverageRating(fishingInstructorAdventure.getAverageRating());

        this.adventureRepository.save(fia);
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

    @Override
    public List<FishingInstructorAdventure> findByInstructor(Long id) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        List<FishingInstructorAdventure> all = this.adventureRepository.findByInstructor(id);
        List<FishingInstructorAdventure> myAdventures = new ArrayList<FishingInstructorAdventure>();

        for (FishingInstructorAdventure fia : all) {
            if (Objects.equals(fia.getInstructor().getId(), instructor.getId())) {
                myAdventures.add(fia);
            }
        }
        return myAdventures;
    }
}
