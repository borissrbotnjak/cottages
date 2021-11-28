package com.isa.cottages.Repository;

import com.isa.cottages.Model.Boat;
import com.isa.cottages.Model.Cottage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoatRepository extends JpaRepository<Boat, Long> {

    @Query(value="SELECT c.* FROM Boat c JOIN USERS bo ON bo.id=c.boat_owner_id WHERE lower(c.boat_name) like lower(concat('%', ?1, '%'))"  +
            "or lower(bo.first_name) like lower(concat('%', ?1, '%'))" +
            "or lower(bo.last_name) like lower(concat('%', ?1, '%'))"
            , nativeQuery = true)
    /*@Query(value="SELECT c.* FROM Boat c WHERE lower(c.boat_name) like lower(concat('%', ?1, '%')) "
            , nativeQuery = true)*/
    List<Boat> findByKeyword(@Param("keyword") String keyword);
    List<Boat> findByOrderByBoatNameDesc();
    List<Boat> findByOrderByBoatNameAsc();
    List<Boat> findByOrderByAverageRatingAsc();
    List<Boat> findByOrderByAverageRatingDesc();
    List<Boat> findByOrderByResidenceAscCityAscStateAsc();
    List<Boat> findByOrderByResidenceDescCityDescStateDesc();
}
