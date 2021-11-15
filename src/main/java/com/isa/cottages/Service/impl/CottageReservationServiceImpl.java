package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.Cottage;
import com.isa.cottages.Model.CottageOwner;
import com.isa.cottages.Model.CottageReservation;
import com.isa.cottages.Repository.CottageReservationRepository;
import com.isa.cottages.Service.CottageReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CottageReservationServiceImpl implements CottageReservationService {

    private CottageReservationRepository reservationRepository;
    private UserServiceImpl userService;
    private CottageServiceImpl cottageService;

    @Autowired
    public CottageReservationServiceImpl(CottageReservationRepository reservationRepository,
                                         UserServiceImpl userService,
                                         CottageServiceImpl cottageService){
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.cottageService = cottageService;
    }

    @Override
    public List<CottageReservation> getUpcomingReservations() throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        List<CottageReservation> all = this.reservationRepository.getAllReserved();
        List<CottageReservation> upcoming = new ArrayList<CottageReservation>();

        for (CottageReservation res: all) {
            if(res.getStartingTime().isAfter(LocalDateTime.now())) {
                upcoming.add(res);
            }
        }
        return upcoming;
    }

    @Override
    public List<CottageReservation> getPastReservations() throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        List<CottageReservation> all = this.reservationRepository.getAllReserved();
        List<CottageReservation> pastOnes = new ArrayList<CottageReservation>();

        for (CottageReservation res:all) {
            if((res.getStartingTime().isBefore(LocalDateTime.now())) && (Objects.equals(res.getCottageOwner().getId(), cottageOwner.getId()))) {
                pastOnes.add(res);
            }
        }
        return pastOnes;
    }

    @Override
    public CottageReservation saveAction(CottageReservation cottageReservation) throws Exception {
        CottageReservation cr = new CottageReservation();

        cr.setActionAvailableFrom(cottageReservation.getActionAvailableFrom());
        cr.setActionAvailableUntil(cottageReservation.getActionAvailableUntil());
        cr.setMaxPersons(cottageReservation.getMaxPersons());
        cr.setPrice(cottageReservation.getPrice());
        cr.setAdditionalServices(cottageReservation.getAdditionalServices());
        cr.setCottageOwner(cottageReservation.getCottageOwner());
        cr.setCottage(cottageReservation.getCottage());
        cr.setAction(true);
        this.reservationRepository.save(cr);

        return cr;
    }

//    @Override
//    public List<Reservation> findReserved(Long id) {
//        List<Reservation> all = this.reservationRepository.findReserved(id);
//        List<Reservation> activeOnes = new ArrayList<>();
//
//        for(Reservation res:all) {
//            if(res.getStartingTime().isAfter(LocalDateTime.now())) {
//                activeOnes.add(res);
//            }
//        }
//        return activeOnes;
//    }

    @Override
    public List<CottageReservation> findByCottage(Long id) throws Exception{
        Cottage cottage = (Cottage) cottageService.findById(id);
        List<CottageReservation> all = this.reservationRepository.findByCottage(id);
        List<CottageReservation> cr = new ArrayList<CottageReservation>();

        for (CottageReservation c:all) {
            if(Objects.equals(c.getCottage().getId(), cottage.getId())) {
                cr.add(c);
            }
        }
        return cr;
    }
}
