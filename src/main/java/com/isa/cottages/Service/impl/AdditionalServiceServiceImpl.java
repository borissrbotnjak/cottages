package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.*;
import com.isa.cottages.Repository.AdditionalServiceRepository;
import com.isa.cottages.Service.AdditionalServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AdditionalServiceServiceImpl implements AdditionalServiceService {

    private AdditionalServiceRepository additionalServiceRepository;
    private CottageServiceImpl cottageService;
    private UserServiceImpl userService;
    private BoatServiceImpl boatService;
    private FishingInstructorAdventureServiceImpl adventureService;

    @Autowired
    public AdditionalServiceServiceImpl (AdditionalServiceRepository additionalServiceRepository,
                                         CottageServiceImpl cottageService, UserServiceImpl userService,
                                         BoatServiceImpl boatService, FishingInstructorAdventureServiceImpl adventureService) {
        this.additionalServiceRepository = additionalServiceRepository;
        this.cottageService = cottageService;
        this.userService = userService;
        this.boatService = boatService;
        this.adventureService = adventureService;
    }


    @Override
    public AdditionalService findOne(Long id) {
        return additionalServiceRepository.getOne(id);
    }

    @Override
    public AdditionalService save(AdditionalService additionalService) throws Exception {
        AdditionalService as = new AdditionalService();
        as.setName(additionalService.getName());
        as.setPrice(additionalService.getPrice());
        as.setCottage(additionalService.getCottage());
        as.setBoat(additionalService.getBoat());

        this.additionalServiceRepository.save(as);

        return as;
    }

    @Override
    public List<AdditionalService> findByAdventure(Long id) throws Exception{
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        FishingInstructorAdventure adventure = adventureService.findById(id);
        List<AdditionalService> all = this.additionalServiceRepository.findByAdventure(id);
        List<AdditionalService> myAdditionalServices = new ArrayList<AdditionalService>();

        for (AdditionalService as:all) {
            if(Objects.equals(as.getCottage().getId(), adventure.getId())) {
                myAdditionalServices.add(as);
            }
        }
        return myAdditionalServices;
    }

    @Override
    public List<AdditionalService> findByCottage(Long id) throws Exception{
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        Cottage cottage = cottageService.findById(id);
        List<AdditionalService> all = this.additionalServiceRepository.findByCottage(id);
        List<AdditionalService> myAdditionalServices = new ArrayList<AdditionalService>();

        for (AdditionalService as:all) {
            if(Objects.equals(as.getCottage().getId(), cottage.getId())) {
                myAdditionalServices.add(as);
            }
        }
        return myAdditionalServices;
    }

    @Override
    public List<AdditionalService> findByBoat(Long id) throws Exception{
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();
        Boat boat = boatService.findById(id);
        List<AdditionalService> all = this.additionalServiceRepository.findByBoat(id);
        List<AdditionalService> myAdditionalServices = new ArrayList<AdditionalService>();

        for (AdditionalService as:all) {
            if(Objects.equals(as.getBoat().getId(), boat.getId())) {
                myAdditionalServices.add(as);
            }
        }
        return myAdditionalServices;
    }
}
