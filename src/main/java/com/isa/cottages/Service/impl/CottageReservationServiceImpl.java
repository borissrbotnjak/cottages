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
    public List<CottageReservation> getAllOwnersReservations(Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();

        return this.reservationRepository.getAllReserved(id);
    }

    @Override
    public List<CottageReservation> getOwnersUpcomingReservations(Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        List<CottageReservation> all = this.reservationRepository.getAllReserved(id);
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
        List<CottageReservation> all = this.reservationRepository.getAllReserved(id);
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
    public List<CottageReservation> findDiscountsByCottage(Long id) throws Exception{
        Cottage cottage = cottageService.findById(id);
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
    public List<CottageReservation> findClient(String keyword) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();

        return this.reservationRepository.findClient(keyword);
    }
}
