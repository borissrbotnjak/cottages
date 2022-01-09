package com.isa.cottages.Service;

import com.isa.cottages.Model.Cottage;
import com.isa.cottages.Model.FishingInstructorAdventure;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface FishingInstructorAdventureService {
    FishingInstructorAdventure findById(Long id) throws Exception;
    List<FishingInstructorAdventure> findAll();
    List<FishingInstructorAdventure> findByKeyword(String keyword);

    List<FishingInstructorAdventure> findAllAvailable(LocalDate startDate, LocalDate endDate, int numOfPersons, Boolean asc, String by) throws Exception;

    FishingInstructorAdventure saveAdventure(FishingInstructorAdventure FishingInstructorAdventure);

    List<FishingInstructorAdventure> sortByInstructorInfo(Boolean asc);
    List<FishingInstructorAdventure> findByOrderByAdventureNameAsc();
    List<FishingInstructorAdventure> findByOrderByAdventureNameDesc();
    List<FishingInstructorAdventure> findByOrderByRatingAsc();
    List<FishingInstructorAdventure> findByOrderByRatingDesc();
    List<FishingInstructorAdventure> findByOrderByAddressAsc();
    List<FishingInstructorAdventure> findByOrderByAddressDesc();
}
