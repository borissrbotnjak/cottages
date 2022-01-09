package com.isa.cottages.Service;

import com.isa.cottages.Model.AdditionalService;

import java.util.Set;

public interface AdditionalServiceService {

    Set<Long> getIds(Set<AdditionalService> services);

}
