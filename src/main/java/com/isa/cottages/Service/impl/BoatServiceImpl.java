package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.Boat;
import com.isa.cottages.Model.BoatOwner;
import com.isa.cottages.Model.BoatReservation;
import com.isa.cottages.Repository.BoatRepository;
import com.isa.cottages.Service.BoatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class BoatServiceImpl implements BoatService {

    @Autowired
    private BoatRepository boatRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private BoatOwnerServiceImpl boatOwnerService;

    @Autowired
    private BoatReservationServiceImpl reservationService;

//    @Autowired
//    public BoatServiceImpl(BoatRepository boatRepository,
//                           UserServiceImpl userService,
//                           BoatOwnerServiceImpl boatOwnerService,
//                           BoatReservationServiceImpl reservationService){
//        this.boatRepository = boatRepository;
//        this.userService = userService;
//        this.boatOwnerService = boatOwnerService;
//        this.reservationService = reservationService;
//    }

    @Override
    public Boat findById(Long id) throws Exception {
        if (this.boatRepository.findById(id).isEmpty()) {
            throw new Exception("No such value(boat service)");
        }
        return this.boatRepository.findById(id).get();
    }

    @Override
    public Boat saveBoat(Boat boat) {
        Boat b = new Boat();
        b.setBoatName(boat.getBoatName());
        b.setEngineType(boat.getEngineType());
        b.setLength(boat.getLength());
        b.setEngineNumber(boat.getEngineNumber());
        b.setEnginePower(boat.getEnginePower());
        b.setMaxSpeed(boat.getMaxSpeed());
        b.setState(boat.getState());
        b.setCity(boat.getCity());
        b.setResidence(boat.getResidence());
        b.setCapacity(boat.getCapacity());
        b.setRules(boat.getRules());
        b.setDescription(boat.getDescription());
        b.setPrice(boat.getPrice());
        b.setBoatOwner(boat.getBoatOwner());
        b.setAvailableFrom(boat.getAvailableFrom());
        b.setAvailableUntil(boat.getAvailableUntil());

        this.boatRepository.save(b);
        return b;
    }

    @Override
    public Boat updateBoat(Boat boat) throws Exception {
        Boat forUpdate = findById(boat.getId());

        forUpdate.setBoatName(boat.getBoatName());
        forUpdate.setLength(boat.getLength());
        forUpdate.setEngineNumber(boat.getEngineNumber());
        forUpdate.setEnginePower(boat.getEnginePower());
        forUpdate.setMaxSpeed(boat.getMaxSpeed());
        forUpdate.setState(boat.getState());
        forUpdate.setCity(boat.getCity());
        forUpdate.setResidence(boat.getResidence());
        forUpdate.setCapacity(boat.getCapacity());
        forUpdate.setRules(boat.getRules());
        forUpdate.setDescription(boat.getDescription());
        forUpdate.setPrice(boat.getPrice());
        forUpdate.setBoatOwner(boat.getBoatOwner());

        this.boatRepository.save(forUpdate);
        return forUpdate;
    }

    @Override
    public void removeBoat(Boat boat, Long oid) throws Exception {
        BoatOwner boatOwner = (BoatOwner) userService.findById(oid);
        if (boatOwner == null) {
            throw new Exception("Boat owner does not exist.");
        }
        Boat b = findById(boat.getId());

        Set<Boat> boats = boatOwner.getBoats();
        boats.remove(b);
        boatOwner.setBoats(boats);
        boat.setDeleted(true);

        b.setBoatOwner(null);
        this.boatOwnerService.updateBoats(boatOwner);
    }

    @Override
    public Boolean canUpdateOrDelete(Long id) throws Exception {
        boolean updateOrDelete = true;
        Boat boat = findById(id);

        if (boat.getReserved() == true) {
            updateOrDelete = false;
        }
        return updateOrDelete;
    }

    @Override
    public Collection<Boat> getAll() {
        return this.boatRepository.findAll();
    }

    @Override
    public List<Boat> findByKeyword(String keyword) {
        return this.boatRepository.findByKeyword(keyword);
    }

    @Override
    public List<Boat> findByBoatOwner(Long id) throws Exception{
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();
        List<Boat> all = this.boatRepository.findByBoatOwner(id);
        List<Boat> myBoats = new ArrayList<Boat>();

        for (Boat bo:all) {
            if(Objects.equals(bo.getBoatOwner().getId(), boatOwner.getId())) {
                myBoats.add(bo);
            }
        }
        return myBoats;
    }

    @Override
    public Boat defineAvailability(Boat boat) throws Exception {
        Boat forUpdate = findById(boat.getId());

        forUpdate.setAvailableFrom(boat.getAvailableFrom());
        forUpdate.setAvailableUntil(boat.getAvailableUntil());

        this.boatRepository.save(forUpdate);
        return forUpdate;
    }

    @Override
    public List<Boat> orderByNameDesc() { return this.boatRepository.findByOrderByBoatNameDesc(); }

    @Override
    public List<Boat> orderByNameAsc() { return this.boatRepository.findByOrderByBoatNameAsc(); }

    @Override
    public List<Boat> orderByRatingAsc() { return this.boatRepository.findByOrderByAverageRatingAsc();  }

    @Override
    public List<Boat> orderByRatingDesc() { return this.boatRepository.findByOrderByAverageRatingDesc();  }

    @Override
    public List<Boat> orderByAddressDesc() { return this.boatRepository.findByOrderByResidenceDescCityDescStateDesc(); }

    @Override
    public List<Boat> orderByAddressAsc() { return this.boatRepository.findByOrderByResidenceAscCityAscStateAsc(); }

    @Override
    public Boolean boatAvailable(LocalDate startDate, LocalDate endDate, Boat boat, int numPersons) {
        if (boat.getNumPersons() >= numPersons) {
            if (boat.getAvailableFrom() != null && boat.getAvailableUntil() != null) {
                if (boat.getAvailableFrom().toLocalDate().isBefore(startDate) && boat.getAvailableUntil().toLocalDate().isAfter(endDate)) { return true; }
            } else { return true; }
        }

        return false;
    }

    @Override
    public Boolean myBoatAvailable(LocalDate startDate, LocalDate endDate, Boat boat, int numPersons) {
        if (boat.getNumPersons() >= numPersons) {
            if (boat.getAvailableFrom() != null && boat.getAvailableUntil() != null) {
                if ((boat.getAvailableFrom().toLocalDate().isBefore(startDate) && boat.getAvailableUntil().toLocalDate().isAfter(endDate))) {
                    return true;
                }
            } else { return true; }
        }   // TODO: OBRISI
        return false;
    }

    @Override
    public Set<Boat> findAllAvailable(LocalDate startDate, LocalDate endDate, int numOfPersons) throws Exception {

        Set<Boat> available = new HashSet<>();
        Set<Boat> withReservation = new HashSet<>();
        List<BoatReservation> reservations = this.reservationService.getAllUpcoming();

        for (BoatReservation res : reservations) {
            withReservation.add(res.getBoat());
            if (this.boatAvailable(startDate, endDate, res.getBoat(), numOfPersons)) {
                if ((res.getBoat().getNumPersons() >= numOfPersons && res.getStartDate().isAfter(endDate) && res.getEndDate().isAfter(endDate)) ||
                        (res.getBoat().getNumPersons() >= numOfPersons && res.getStartDate() == null && res.getEndDate() == null) ||
                        (res.getBoat().getNumPersons() >= numOfPersons && res.getStartDate().isBefore(startDate) && res.getEndDate().isBefore(startDate))) {
                    available.add(res.getBoat());
                }
            }
        }

        // ako ne postoji rezervacija i dobar je kapacitet, dodaj
        List<Boat> all = this.boatRepository.findAll();

        HashSet<Boat> allSet = new HashSet<>(all);

        HashSet<Boat> woReservation = new HashSet<>(allSet) {{ removeAll(withReservation); }};

        for (Boat b : woReservation) {
            if (b.getNumPersons() >= numOfPersons) { available.add(b); }
        }

        return available;
    }

    @Override
    public List<Boat> findAllAvailableSorted(LocalDate startDate, LocalDate endDate, int numOfPersons, Boolean asc, Boolean price, Boolean rating) throws Exception {
        Set<Boat> set = this.findAllAvailable(startDate, endDate, numOfPersons);
        List<Boat> available = new ArrayList<>(set);

        if (asc && price && !rating) {
            available.sort(Comparator.comparing(Boat::getPrice));
        }
        else if (asc && !price && rating) {
            available.sort(Comparator.comparing(Boat::getAverageRating));
        }
        else if (!asc && price && !rating) {
            available.sort(Comparator.comparing(Boat::getPrice).reversed());
        }
        else if (!asc && !price && rating) {
            available.sort(Comparator.comparing(Boat::getAverageRating).reversed());
        }

        return available;
    }
}
