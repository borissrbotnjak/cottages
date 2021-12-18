package com.isa.cottages.Repository;

import com.isa.cottages.Model.AdditionalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalServiceRepository extends JpaRepository<AdditionalService, Long> {
}
