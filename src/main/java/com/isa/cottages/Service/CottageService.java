package com.isa.cottages.Service;

import com.isa.cottages.Model.Cottage;
import java.util.Collection;
import java.util.List;

public interface CottageService {

    Cottage findById(Long id) throws Exception;

    Cottage saveCottage(Cottage cottage);

    Collection<Cottage> findAll();

    List<Cottage> findByKeyword(String keyword);
}
