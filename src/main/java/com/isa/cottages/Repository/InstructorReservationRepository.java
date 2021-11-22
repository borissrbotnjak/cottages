package com.isa.cottages.Repository;

import com.isa.cottages.Model.BoatReservation;
import com.isa.cottages.Model.InstructorReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructorReservationRepository extends JpaRepository<InstructorReservation, Long> {

    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=true " +
            "and res.instructor_id is not null", nativeQuery = true)
    List<InstructorReservation> getAllReservations();
}
