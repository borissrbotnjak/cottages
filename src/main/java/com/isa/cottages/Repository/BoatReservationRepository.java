package com.isa.cottages.Repository;

import com.isa.cottages.Model.Boat;
import com.isa.cottages.Model.BoatReservation;
import com.isa.cottages.Model.CottageReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoatReservationRepository extends JpaRepository<BoatReservation, Long> {

    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=true " +
            "and res.boat_id is not null", nativeQuery = true)
    List<BoatReservation> getAllReservations();
}
