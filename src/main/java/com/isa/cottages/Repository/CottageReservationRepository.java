package com.isa.cottages.Repository;

import com.isa.cottages.Model.Cottage;
import com.isa.cottages.Model.CottageReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CottageReservationRepository extends JpaRepository<CottageReservation, Long> {

//    @Query(value = "SELECT * FROM Reservation res WHERE res.reserved = true", nativeQuery = true)
//    List<Reservation> findReserved(@Param("id") Long id);

    @Query(value = "SELECT * FROM Reservation res WHERE res.deleted=false and res.reserved=true", nativeQuery = true)
    List<CottageReservation> getAllReserved();

    @Query(value = "SELECT * FROM Cottage_reservation c WHERE c.cottage_id = ?1", nativeQuery = true)
    List<CottageReservation> findByCottage(@Param("id") Long id);

}