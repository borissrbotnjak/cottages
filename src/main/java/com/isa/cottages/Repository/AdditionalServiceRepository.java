package com.isa.cottages.Repository;

import com.isa.cottages.Model.AdditionalService;
import com.isa.cottages.Model.FishingInstructorAdventure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdditionalServiceRepository extends JpaRepository<AdditionalService, Long> {


    List<AdditionalService> findAllByAdventure(FishingInstructorAdventure fishingInstructorAdventure);
}
