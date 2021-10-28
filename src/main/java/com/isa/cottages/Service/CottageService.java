package com.isa.cottages.Service;

import com.isa.cottages.Model.Cottage;

public interface CottageService {

    Cottage findById(Long id) throws Exception;

    Cottage saveCottage(Cottage cottage);
}
