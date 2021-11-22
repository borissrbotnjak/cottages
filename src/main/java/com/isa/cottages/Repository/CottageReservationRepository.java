package com.isa.cottages.Repository;

import com.isa.cottages.Model.CottageReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CottageReservationRepository extends JpaRepository<CottageReservation, Long> {

    @Query(value = "SELECT * FROM Cottage_reservation res WHERE res.deleted=false and res.reserved=true " +
            "and res.cottage_owner_id = ?1", nativeQuery = true)
    List<CottageReservation> getAllReserved(@Param("id") Long id);

    @Query(value = "SELECT * FROM Cottage_reservation res WHERE res.deleted=false and res.reserved=true " +
            "and res.boat_id is not null", nativeQuery = true)
    List<CottageReservation> getAllBoatReservations();

    @Query(value = "SELECT * FROM Cottage_reservation c WHERE c.cottage_id = ?1 and " +
            "c.discount = true", nativeQuery = true)
    List<CottageReservation> findDiscountsByCottage(@Param("id") Long id);

    @Query(value = "SELECT FROM Cottage_reservation res JOIN Users u ON res.client_id=u.id WHERE lower(u.first_name) like lower(concat('%', ?1, '%'))",
    nativeQuery = true)
//    @Query(value = "select u.* from Cottage_reservation res left outer join users u" +
//            "on res.client_id = u.id" +
//            "group by u.id " +
//            "WHERE lower(u.first_name) like lower(concat('%', ?1, '%'))", nativeQuery = true)
    List<CottageReservation> findClient(@Param("keyword") String keyword);
}