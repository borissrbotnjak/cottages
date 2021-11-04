package com.isa.cottages.Repository;

import com.isa.cottages.Model.Cottage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CottageRepository extends JpaRepository<Cottage, Long> {
//
//    @Query(value = "SELECT * from Cottage cot WHERE cot.cottage_owner_id = ?1", nativeQuery = true)
//    List<Cottage> findAllByCottageOwner(@Param("cotOwnId") Long id);

    @Query(value="SELECT * FROM Cottage c where lower(c.name) like lower(concat('%', ?1, '%')) " +
//            "or lower(c.state) like lower(concat('%', ?1, '%')" +
//            "or lower(c.city) like lower(concat('%', ?1, '%')" +
            //Ne radi i za city i state ????
            "or lower(c.residence) like lower(concat('%', ?1, '%')) "
           , nativeQuery = true)
    List<Cottage> findByKeyword(@Param("keyword") String keyword);
}
