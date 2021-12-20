package com.isa.cottages.Service;

import com.isa.cottages.Model.BoatOwner;

public interface BoatOwnerService {


    BoatOwner findById(Long id) throws Exception;
    BoatOwner updateBoats(BoatOwner boatOwner) throws Exception;
    BoatOwner updateProfile(BoatOwner boatOwner) throws Exception;


}
