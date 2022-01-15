package com.isa.cottages.Service;

import com.isa.cottages.Model.AdditionalService;

import java.util.List;

public interface AdditionalServiceService {

    AdditionalService findOne(Long id);
    AdditionalService save (AdditionalService additionalService) throws Exception;

    List<AdditionalService> findByCottage(Long id) throws Exception;
    List<AdditionalService> findByBoat(Long id) throws Exception;

    Set<Long> getIds(Set<AdditionalService> services);


}
