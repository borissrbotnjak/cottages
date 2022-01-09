package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.AdditionalService;
import com.isa.cottages.Service.AdditionalServiceService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AdditionalServiceServiceImpl implements AdditionalServiceService {
    @Override
    public Set<Long> getIds(Set<AdditionalService> services) {
        Set<Long> ids = new HashSet<>();
        for (AdditionalService s : services) { ids.add(s.getId()); }
        return ids;
    }
}
