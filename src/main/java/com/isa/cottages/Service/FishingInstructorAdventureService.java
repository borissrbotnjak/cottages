package com.isa.cottages.Service;

import com.isa.cottages.Model.AdditionalService;
import com.isa.cottages.Model.FishingInstructorAdventure;

import java.util.List;

public interface FishingInstructorAdventureService {
    FishingInstructorAdventure findById(Long id) throws Exception;

    List<FishingInstructorAdventure> findAll();

    Boolean canUpdateOrDelete(Long id) throws Exception;

    List<FishingInstructorAdventure> findByKeyword(String keyword);

    List<AdditionalService> findServicesByAdventure(FishingInstructorAdventure adventure);

    AdditionalService saveService(AdditionalService additionalService);

    FishingInstructorAdventure updateAdventure(FishingInstructorAdventure adventure) throws Exception;

    FishingInstructorAdventure saveAdventure(FishingInstructorAdventure FishingInstructorAdventure);

    List<FishingInstructorAdventure> sortByInstructorInfo(Boolean asc);

    List<FishingInstructorAdventure> findByInstructor(Long id) throws Exception;
}
