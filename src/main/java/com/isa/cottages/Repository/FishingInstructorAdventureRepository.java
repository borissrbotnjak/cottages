package com.isa.cottages.Repository;

import com.isa.cottages.Model.FishingInstructorAdventure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FishingInstructorAdventureRepository extends JpaRepository<FishingInstructorAdventure, Long> {

    Optional<FishingInstructorAdventure> findById(Long id);
}
