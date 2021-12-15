package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.AdditionalService;
import com.isa.cottages.Model.FishingInstructorAdventure;
import com.isa.cottages.Model.Instructor;
import com.isa.cottages.Repository.AdditionalServiceRepository;
import com.isa.cottages.Repository.FishingInstructorAdventureRepository;
import com.isa.cottages.Service.FishingInstructorAdventureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FishingInstructorAdventureServiceImpl implements FishingInstructorAdventureService {

    private final FishingInstructorAdventureRepository adventureRepository;
    private final AdditionalServiceRepository serviceRepository;
    private final UserServiceImpl userService;

    @Autowired
    public FishingInstructorAdventureServiceImpl(FishingInstructorAdventureRepository adventureRepository, AdditionalServiceRepository serviceRepository, UserServiceImpl userService) {
        this.adventureRepository = adventureRepository;
        this.userService = userService;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public FishingInstructorAdventure findById(Long id) throws Exception {
        if (this.adventureRepository.findById(id).isEmpty()) {
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
        boolean updateOrDelete = true;
        FishingInstructorAdventure adventure = findById(id);

        if (adventure.getReserved() != null) {
            updateOrDelete = false;
        }
        return updateOrDelete;
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
    public FishingInstructorAdventure updateAdventure(FishingInstructorAdventure adventure) throws Exception {
        FishingInstructorAdventure forUpdate = findById(adventure.getId());

        forUpdate.setAdventureName(adventure.getAdventureName());
        forUpdate.setAdventureResidence(adventure.getAdventureResidence());
        forUpdate.setAdventureCity(adventure.getAdventureCity());
        forUpdate.setAdventureState(adventure.getAdventureState());
        forUpdate.setAdventureDescription(adventure.getAdventureDescription());
        forUpdate.setMaxClients(adventure.getMaxClients());
        forUpdate.setQuickReservation(adventure.getQuickReservation());
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
        return forUpdate;
    }


    @Override
    public FishingInstructorAdventure saveAdventure(FishingInstructorAdventure fishingInstructorAdventure) {
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
        return fia;
    }

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
