package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.Boat;
import com.isa.cottages.Model.BoatReservation;
import com.isa.cottages.Repository.BoatRepository;
import com.isa.cottages.Service.BoatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class BoatServiceImpl implements BoatService {

    private BoatRepository boatRepository;

    private BoatReservationServiceImpl reservationService;


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
        this.boatRepository.save(b);
        return b;
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
                if ((boat.getAvailableFrom().toLocalDate().isAfter(startDate) && boat.getAvailableUntil().toLocalDate().isAfter(endDate))
                        || (boat.getAvailableFrom().toLocalDate().isBefore(startDate) && boat.getAvailableUntil().toLocalDate().isBefore(endDate))) {
                    return true;
                }
            } else { return true; }
        }

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
