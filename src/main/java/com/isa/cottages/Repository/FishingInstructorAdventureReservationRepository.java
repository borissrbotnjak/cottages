package com.isa.cottages.Repository;

import com.isa.cottages.Model.AdventureReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    @Query(value = "SELECT * FROM reservation c WHERE c.adventure_id = ?1 and " +
            "c.discount = true and c.deleted=false", nativeQuery = true)
    List<AdventureReservation> findDiscountsByAdventure(@Param("id") Long id);

    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=false " +
            "and res.adventure_id=?1 and res.discount = true", nativeQuery = true)
    List<AdventureReservation> findAllWithDiscount(Long adventureId);

    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=true " +
            "and res.adventure_id is not null " +
            "and not (res.start_date < ?2 and res.end_date > ?1 )" +
            "and res.num_persons >= ?3 and res.instructor_id=?4", nativeQuery = true)
    List<AdventureReservation> findAllMyAvailable(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                                                  @Param("capacity") int capacity, @Param("id") Long id);

    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=true " +
            "and res.adventure_id is not null " +
            "and res.start_date <= ?2 and res.end_date > ?1 and res.instructor_id=?3", nativeQuery = true)
    List<AdventureReservation> findAllMyUnavailable(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                                               @Param("id") Long id);
}
