package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.Boat;
import com.isa.cottages.Repository.BoatRepository;
import com.isa.cottages.Service.BoatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoatServiceImpl implements BoatService {

    private BoatRepository boatRepository;

    @Autowired
    public BoatServiceImpl(BoatRepository boatRepository){
        this.boatRepository = boatRepository;
    }

    @Override
    public Boat findById(Long id) throws Exception {
        if (!this.boatRepository.findById(id).isPresent()) {
            throw new Exception("No such value(boat service)");
        }
        return this.boatRepository.findById(id).get();
    }

    @Override
    public Boat saveBoat(Boat boat) {
        Boat b = new Boat();
        b.setName(boat.getName());
        this.boatRepository.save(b);
        return b;
    }
}
