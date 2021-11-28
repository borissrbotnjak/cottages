package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.FishingInstructorAdventure;
import com.isa.cottages.Repository.FishingInstructorAdventureRepository;
import com.isa.cottages.Service.FishingInstructorAdventureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FishingInstructorAdventureServiceImpl implements FishingInstructorAdventureService {

    private final FishingInstructorAdventureRepository adventureRepository;

    @Autowired
    public FishingInstructorAdventureServiceImpl(FishingInstructorAdventureRepository adventureRepository) {
        this.adventureRepository = adventureRepository;
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
    public List<FishingInstructorAdventure> findByKeyword(String keyword) { return this.adventureRepository.findByKeyword(keyword); }

    @Override
    public FishingInstructorAdventure saveAdventure(FishingInstructorAdventure fishingInstructorAdventure) {
        FishingInstructorAdventure fia = new FishingInstructorAdventure();

        fia.setAdventureName(fishingInstructorAdventure.getAdventureName());
        fia.setAdventureCity(fishingInstructorAdventure.getAdventureCity());
        fia.setAdventureState(fishingInstructorAdventure.getAdventureState());
        fia.setAdventureResidence(fishingInstructorAdventure.getAdventureResidence());

        fia.setAdventureDescription(fishingInstructorAdventure.getAdventureDescription());
        fia.setAverageRating(fishingInstructorAdventure.getAverageRating());

        this.adventureRepository.save(fia);
        return fia;
    }

    @Override
    public List<FishingInstructorAdventure> findByOrderByAdventureNameAsc() { return this.adventureRepository.findByOrderByAdventureNameAsc(); }

    @Override
    public List<FishingInstructorAdventure> findByOrderByAdventureNameDesc() { return this.adventureRepository.findByOrderByAdventureNameDesc(); }

    @Override
    public List<FishingInstructorAdventure> findByOrderByRatingAsc() { return this.adventureRepository.findByOrderByAverageRatingAsc(); }

    @Override
    public List<FishingInstructorAdventure> findByOrderByRatingDesc() { return this.adventureRepository.findByOrderByAverageRatingDesc(); }

    @Override
    public List<FishingInstructorAdventure> findByOrderByAddressAsc() { return this.adventureRepository.findByOrderByAdventureResidenceAscAdventureCityAscAdventureStateAsc(); }

    @Override
    public List<FishingInstructorAdventure> findByOrderByAddressDesc() { return this.adventureRepository.findByOrderByAdventureResidenceDescAdventureCityDescAdventureStateDesc(); }

    @Override
    public List<FishingInstructorAdventure> sortByInstructorInfo(Boolean asc) {
        if (asc) {
            return this.adventureRepository.findByOrderByInstructorInfoAsc();
        } else {
            return this.adventureRepository.findByOrderByInstructorInfoDesc();
        }
    }
}
