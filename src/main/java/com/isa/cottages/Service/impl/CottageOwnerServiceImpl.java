package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.CottageOwner;
import com.isa.cottages.Repository.CottageOwnerRepository;
import com.isa.cottages.Service.CottageOwnerService;
import org.springframework.stereotype.Service;

@Service
public class CottageOwnerServiceImpl implements CottageOwnerService {

    private CottageOwnerRepository cottageOwnerRepository;

    @Override
    public CottageOwner findById(Long id) throws Exception {
        if(this.cottageOwnerRepository.findById(id).isEmpty()) {
            throw new Exception("No such value");
        }
//        if(cottageOwner == null) {
//            throw new Exception("Cottage owner with this id does not exist.");
//        }
        return this.cottageOwnerRepository.findById(id).get();
    }

    @Override
    public CottageOwner updateProfile(CottageOwner cottageOwner) throws Exception {
        CottageOwner forUpdate = this.findById(cottageOwner.getId());

        forUpdate.setFirstName(cottageOwner.getFirstName());
        forUpdate.setLastName(cottageOwner.getLastName());
        forUpdate.setPhoneNumber(cottageOwner.getPhoneNumber());
        forUpdate.setResidence(cottageOwner.getResidence());
        forUpdate.setCity(cottageOwner.getCity());
        forUpdate.setState(cottageOwner.getState());

        this.cottageOwnerRepository.save(forUpdate);
        return forUpdate;

    }
}
