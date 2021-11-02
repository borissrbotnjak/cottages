package com.isa.cottages.Service;

import com.isa.cottages.Model.Cottage;

import java.util.List;

public interface CottageService {

    Cottage findById(Long id) throws Exception;

    Cottage saveCottage(Cottage cottage);

//    List<Cottage> findAllByCottageOwner(Long id);

    List<Cottage> findAll();
}
