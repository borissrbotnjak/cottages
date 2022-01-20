package com.isa.cottages.Repository;

import com.isa.cottages.Model.BoatReservation;
import com.isa.cottages.Model.CottageReservation;
import com.isa.cottages.Model.InstructorReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InstructorReservationRepository extends JpaRepository<InstructorReservation, Long> {

    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=true " +
            "and res.instructor_id is not null", nativeQuery = true)
    List<InstructorReservation> getAllReservations();

    @Query(value = "SELECT * FROM RESERVATION RES WHERE RES.DELETED=FALSE AND RES.RESERVED=TRUE" +
            "AND RES.INSTRUCTOR_ID IS NOT NULL AND RES.CLIENT_ID=?1", nativeQuery = true)
    List<InstructorReservation> findAllByClient(@Param("client_id") Long clientId);

    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=true " +
            "and res.instructor_id is not null", nativeQuery = true)
    List<InstructorReservation> getAllReserved();

    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=false " +
            "and res.instructor_id=?1 and res.discount = true", nativeQuery = true)
    List<InstructorReservation> findAllWithDiscount(Long instructorId);

    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=true " +
            "and res.instructor_id is not null " +
            "and not (res.start_date < ?2 and res.end_date > ?1 )" +
            "and res.num_persons >= ?3 ", nativeQuery = true)
    List<InstructorReservation> findAllAvailable(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                                           @Param("capacity") int capacity);

    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=true " +
            "and res.instructor_id is not null " +
            "and res.start_date < ?2 and res.end_date > ?1 ", nativeQuery = true)
    List<InstructorReservation> findAllUnavailable(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
