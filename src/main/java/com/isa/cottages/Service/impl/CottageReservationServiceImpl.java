package com.isa.cottages.Service.impl;

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

    @Autowired
    public CottageReservationServiceImpl(CottageReservationRepository reservationRepository, UserServiceImpl userService){
        this.reservationRepository = reservationRepository;
        this.userService = userService;
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
}
