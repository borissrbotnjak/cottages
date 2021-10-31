package com.isa.cottages.Service;

import com.isa.cottages.Model.Cottage;

import java.util.List;

public interface CottageService {

    Cottage findById(Long id) throws Exception;
    List<Cottage> findAll();

    Cottage saveCottage(Cottage cottage);

    List<Cottage> findByKeyword(String keyword);
}
