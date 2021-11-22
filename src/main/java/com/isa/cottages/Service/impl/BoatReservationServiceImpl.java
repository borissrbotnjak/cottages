package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.Client;
import com.isa.cottages.Model.CottageReservation;
import com.isa.cottages.Repository.BoatReservationRepository;
import com.isa.cottages.Service.BoatReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BoatReservationServiceImpl implements BoatReservationService {

    private ClientServiceImpl clientService;
    private UserServiceImpl userService;
    private BoatReservationRepository reservationRepository;

    @Autowired
    public BoatReservationServiceImpl(ClientServiceImpl clientService, UserServiceImpl userService, BoatReservationRepository boatReservationRepository) {
        this.clientService = clientService;
        this.userService = userService;
        this.reservationRepository = boatReservationRepository;
    }

    @Override
    public List<CottageReservation> getPastBoatReservations() throws Exception {
        Client cl = this.clientService.findByEmail(this.userService.getUserFromPrincipal().getEmail());
        List<CottageReservation> all = this.reservationRepository.getAllBoatReservations();
        List<CottageReservation> pastOnes = new ArrayList<>();

        for (CottageReservation res : all) {
            if (!res.getDiscount() && /*(res.getBoat() != null) &&*/ (res.getStartingTime().isBefore(LocalDateTime.now())) && (res.getEndTime().isBefore(LocalDateTime.now()))
                    && (Objects.equals(res.getClient().getId(), cl.getId()))) {
                pastOnes.add(res);
            }
        }
        return pastOnes;
    }
}
