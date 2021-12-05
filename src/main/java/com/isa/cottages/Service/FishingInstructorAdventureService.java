package com.isa.cottages.Service;

import com.isa.cottages.Model.FishingInstructorAdventure;

import java.util.List;

public interface FishingInstructorAdventureService {
    FishingInstructorAdventure findById(Long id) throws Exception;
    List<FishingInstructorAdventure> findAll();
    List<FishingInstructorAdventure> findByKeyword(String keyword);
    FishingInstructorAdventure saveAdventure(FishingInstructorAdventure FishingInstructorAdventure);

    List<FishingInstructorAdventure> sortByInstructorInfo(Boolean asc);

    List<FishingInstructorAdventure> findByInstructor(Long id) throws Exception;
}
