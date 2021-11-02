package com.isa.cottages.Service;

import com.isa.cottages.Model.CottageOwner;

public interface CottageOwnerService {

    CottageOwner findById(Long id) throws Exception;

    CottageOwner updateProfile(CottageOwner cottageOwner) throws Exception;
}
