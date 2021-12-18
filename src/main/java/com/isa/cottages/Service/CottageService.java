package com.isa.cottages.Service;

import com.isa.cottages.Model.AdditionalService;
import com.isa.cottages.Model.Cottage;

import java.util.Collection;
import java.util.List;

public interface CottageService {

    Cottage findById(Long id) throws Exception;
    Collection<Cottage> findAll();

    List<Cottage> findByCottageOwner(Long id) throws Exception;

    List<Cottage> findByKeyword(String keyword);
    List<Cottage> findByKeywordAndCottageOwner(String keyword, Long id) throws Exception;

    Cottage defineAvailability(Cottage cottage) throws Exception;

    Cottage saveCottage(Cottage cottage) throws Exception;
    AdditionalService saveAdditionalService(AdditionalService additionalService) throws Exception;
    Cottage updateCottage(Cottage cottage) throws Exception;
    void removeCottage(Cottage cottage, Long oid) throws Exception;

    Boolean canUpdateOrDelete(Long id) throws Exception;

    List<Cottage> orderByNameDesc();
    List<Cottage> orderByNameAsc();
    List<Cottage> orderByRatingAsc();
    List<Cottage> orderByRatingDesc();
    List<Cottage> orderByAddressDesc();
    List<Cottage> orderByAddressAsc();
}
