package com.isa.cottages.Service;

import com.isa.cottages.Model.AdditionalService;
import com.isa.cottages.Model.FishingInstructorAdventure;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface FishingInstructorAdventureService {
    FishingInstructorAdventure findById(Long id) throws Exception;

    List<FishingInstructorAdventure> findAll();

    Boolean canUpdateOrDelete(Long id) throws Exception;

    FishingInstructorAdventure defineAvailability(FishingInstructorAdventure adventure) throws Exception;

    List<FishingInstructorAdventure> findByKeyword(String keyword);

    List<AdditionalService> findServicesByAdventure(FishingInstructorAdventure adventure);

    AdditionalService saveService(AdditionalService additionalService);

    Set<FishingInstructorAdventure> findAllAvailable(LocalDate startDate, LocalDate endDate, int numOfPersons) throws Exception;

    void updateAdventure(FishingInstructorAdventure adventure) throws Exception;

    Boolean adventureAvailable(LocalDate startDate, LocalDate endDate, FishingInstructorAdventure adventure, int numPersons);

    Boolean myAdventureAvailable(LocalDate startDate, LocalDate endDate, FishingInstructorAdventure adventure, Long id) throws Exception;

    Set<FishingInstructorAdventure> findAllMyAvailable(LocalDate startDate, LocalDate endDate, int numOfPersons, Long id) throws Exception;

    Boolean isByInstructor(Long id, FishingInstructorAdventure adventure) throws Exception;

    List<FishingInstructorAdventure> findAllMyAvailableSorted(Long oid, LocalDate startDate, LocalDate endDate, int numOfPersons,
                                                              Boolean asc, Boolean price, Boolean rating) throws Exception;

    void removeAdventure(FishingInstructorAdventure adventure)throws Exception;

    void saveAdventure(FishingInstructorAdventure FishingInstructorAdventure);

    List<FishingInstructorAdventure> sortByInstructorInfo(Boolean asc);
    List<FishingInstructorAdventure> findByOrderByAdventureNameAsc();
    List<FishingInstructorAdventure> findByOrderByAdventureNameDesc();
    List<FishingInstructorAdventure> findByOrderByRatingAsc();
    List<FishingInstructorAdventure> findByOrderByRatingDesc();
    List<FishingInstructorAdventure> findByOrderByAddressAsc();
    List<FishingInstructorAdventure> findByOrderByAddressDesc();

    List<FishingInstructorAdventure> findByInstructor(Long id) throws Exception;
}
