package com.isa.cottages.Repository;

import com.isa.cottages.Model.AdventureReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdventureReservationRepository extends JpaRepository<AdventureReservation, Long> {

    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=true " +
            "and res.instructor_id is not null", nativeQuery = true)
    List<AdventureReservation> getAllReservations();

    @Query(value = "SELECT * FROM RESERVATION RES WHERE RES.DELETED=FALSE AND RES.RESERVED=TRUE" +
            "AND RES.INSTRUCTOR_ID IS NOT NULL AND RES.CLIENT_ID=?1", nativeQuery = true)
    List<AdventureReservation> findAllByClient(@Param("client_id") Long clientId);
/*
    List<AdventureReservation> findByOrderByStartTimeAsc();
    List<AdventureReservation> findByOrderByStartTimeDesc();
    List<AdventureReservation> findByOrderByDurationAsc();
    List<AdventureReservation> findByOrderByDurationDesc();
    List<AdventureReservation> findByOrderByPriceAsc();
    List<AdventureReservation> findByOrderByPriceDesc();*/

}
