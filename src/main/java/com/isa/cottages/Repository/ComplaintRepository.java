package com.isa.cottages.Repository;

import com.isa.cottages.Model.Complaint;
import com.isa.cottages.Model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint,Long> {
    @Query(value = "SELECT * FROM complaint c WHERE c.is_answered = FALSE ", nativeQuery = true)
    List<Complaint> findComplaintByIsAnsweredTrue();

}
