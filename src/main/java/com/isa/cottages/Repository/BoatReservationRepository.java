package com.isa.cottages.Repository;

import com.isa.cottages.Model.Boat;
import com.isa.cottages.Model.BoatReservation;
import com.isa.cottages.Model.Client;
import com.isa.cottages.Model.CottageReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoatReservationRepository extends JpaRepository<BoatReservation, Long> {

    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=true " +
            "and res.boat_owner_id = ?1", nativeQuery = true)
    List<BoatReservation> getAllReservedByOwner(@Param("id") Long id);

    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=true " +
            "and res.boat_id is not null", nativeQuery = true)
    List<BoatReservation> getAllReservations();

    @Query(value = "SELECT * FROM RESERVATION RES WHERE RES.DELETED=FALSE AND RES.RESERVED=TRUE" +
            "AND RES.BOAT_ID IS NOT NULL AND RES.CLIENT_ID=?1", nativeQuery = true)
    List<BoatReservation> findAllByClient(@Param("client_id") Long clientId);
/*
    @Query(value = "SELECT * FROM reservation res WHERE res.deleted=false and res.reserved=true"  +
            "and res.boat_id is not null and res.start_time <= CURRENT_TIMESTAMP " +
            "AND res.end_time <= CURRENT_TIMESTAMP", nativeQuery = true)
    List<BoatReservation> findByOrderByStartTimeAsc();
    List<BoatReservation> findByOrderByStartTimeDesc();
    List<BoatReservation> findByOrderByDurationAsc();
    List<BoatReservation> findByOrderByDurationDesc();
    List<BoatReservation> findByOrderByPriceAsc();
    List<BoatReservation> findByOrderByPriceDesc();*/

    @Query(value = "SELECT * FROM reservation b WHERE b.boat_id = ?1 and " +
            "b.discount = true", nativeQuery = true)
    List<BoatReservation> findDiscountsByBoat(@Param("id") Long id);

    //    @Query(value = "SELECT * FROM reservation res JOIN Users u ON res.client_id=u.id WHERE lower(u.first_name) like lower(concat('%', ?1, '%')) " +
//            "and reservation_type like 'cottage_reservation'",
            @Query(value = "SELECT * FROM users u WHERE lower(u.first_name) like lower(concat('%', ?1, '%'))",
                    nativeQuery = true)
//    @Query(value = "select u.* from reservation res left outer join users u" +
//            "on res.client_id = u.id" +
//            "group by u.id " +
//            "WHERE lower(u.first_name) like lower(concat('%', ?1, '%'))", nativeQuery = true)
                    List<BoatReservation> findClient(@Param("keyword") String keyword);
}

