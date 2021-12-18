package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.Boat;
import com.isa.cottages.Model.BoatOwner;
import com.isa.cottages.Repository.BoatRepository;
import com.isa.cottages.Service.BoatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BoatServiceImpl implements BoatService {

    private BoatRepository boatRepository;
    private UserServiceImpl userService;
    private BoatOwnerServiceImpl boatOwnerService;

    @Autowired
    public BoatServiceImpl(BoatRepository boatRepository,
                           UserServiceImpl userService,
                           BoatOwnerServiceImpl boatOwnerService){
        this.boatRepository = boatRepository;
        this.userService = userService;
        this.boatOwnerService = boatOwnerService;
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
        b.setBoatName(boat.getBoatName());
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
        b.setBoatOwner(boat.getBoatOwner());

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
}
