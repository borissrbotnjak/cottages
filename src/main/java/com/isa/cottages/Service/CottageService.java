package com.isa.cottages.Service;

import com.isa.cottages.Model.Cottage;
import com.isa.cottages.Model.CottageOwner;

import java.util.Collection;
import java.util.List;

public interface CottageService {

    Cottage findById(Long id) throws Exception;

    Cottage saveCottage(Cottage cottage) throws Exception;

    Collection<Cottage> findAll();

    List<Cottage> findByCottageOwner(Long id) throws Exception;

    List<Cottage> findByKeyword(String keyword);
    List<Cottage> findByKeywordAndCottageOwner(String keyword, Long id) throws Exception;

    Cottage updateCottage(Cottage cottage) throws Exception;
    CottageOwner removeCottage(Cottage cottage, CottageOwner cottageOwner) throws Exception;
}
