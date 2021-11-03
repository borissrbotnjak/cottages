package com.isa.cottages.Service;

import com.isa.cottages.Model.Cottage;

import java.util.Collection;
import java.util.List;

public interface CottageService {

    Cottage findById(Long id) throws Exception;

    Cottage saveCottage(Cottage cottage);

//    List<Cottage> findByCottageOwner(Long id);

    Collection<Cottage> findAll();

    List<Cottage> findByKeyword(String keyword);
}
