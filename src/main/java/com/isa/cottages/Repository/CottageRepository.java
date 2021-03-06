package com.isa.cottages.Repository;

import com.isa.cottages.Model.Cottage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CottageRepository extends JpaRepository<Cottage, Long> {
}
