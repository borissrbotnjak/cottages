package com.isa.cottages.Repository;

import com.isa.cottages.Model.Report;
import com.isa.cottages.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query(value = "SELECT * FROM review r WHERE r.approved = FALSE", nativeQuery = true)
    List<Review> findReviewByApprovedIsFalse();
}

