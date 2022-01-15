package com.isa.cottages.Repository;

import com.isa.cottages.Model.BoatReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoatReservationRepository extends JpaRepository<BoatReservation, Long> {

    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=true " +
            "and res.boat_id is not null", nativeQuery = true)
    List<BoatReservation> getAllReservations();

    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=true " +
            "and res.client_id=?1 and res.boat_id is not null", nativeQuery = true)
    List<BoatReservation> findAllByClient(@Param("client_id") Long clientId);

    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=false " +
            "and res.boat_id=?1 and res.discount = true", nativeQuery = true)
    List<BoatReservation> findAllWithDiscount(@Param("boatId") Long boatId);

    void deleteById(Long id);
}
