package com.isa.cottages.Repository;

import com.isa.cottages.Model.AdventureReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FishingInstructorAdventureReservationRepository extends JpaRepository<AdventureReservation, Long> {
    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=true " +
            "and res.instructor_id = ?1", nativeQuery = true)
    List<AdventureReservation> getAllReservedByInstructor(@Param("id") Long id);

    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false " +
            "and res.instructor_id = ?1", nativeQuery = true)
    List<AdventureReservation> getAllInstructorsReservations(@Param("id") Long id);

    @Query(value = "SELECT * FROM RESERVATION RES WHERE RES.DELETED=FALSE AND RES.RESERVED=TRUE" +
            "AND RES.instructor_id IS NOT NULL AND RES.CLIENT_ID=?1", nativeQuery = true)
    List<AdventureReservation> findAllByClient(@Param("client_id") Long clientId);

    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=true " +
            "and res.instructor_id is not null", nativeQuery = true)
    List<AdventureReservation> getAllReserved();

    @Query(value = "SELECT * FROM reservation res JOIN Users u ON res.client_id=u.id WHERE lower(u.first_name) like lower(concat('%', ?1, '%')) " +
            "and reservation_type like 'adventure_reservation' and res.reserved=true and res.deleted = false", nativeQuery = true)
    List<AdventureReservation> findClient(@Param("keyword") String keyword);
}
