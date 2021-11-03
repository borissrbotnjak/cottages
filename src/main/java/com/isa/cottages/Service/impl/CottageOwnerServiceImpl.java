package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.Cottage;
import com.isa.cottages.Model.CottageOwner;
import com.isa.cottages.Repository.CottageOwnerRepository;
import com.isa.cottages.Service.CottageOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CottageOwnerServiceImpl implements CottageOwnerService {

    private CottageOwnerRepository cottageOwnerRepository;

    @Autowired
    public CottageOwnerServiceImpl(CottageOwnerRepository cottageOwnerRepository) {
        this.cottageOwnerRepository = cottageOwnerRepository;
    }

    @Override
    public CottageOwner findById(Long id) throws Exception {
        if (this.cottageOwnerRepository.findById(id).isEmpty()) {
            throw new Exception("No such value(CottageOwner service)");
        }
        return this.cottageOwnerRepository.findById(id).get();
    }

    @Override
    public CottageOwner updateProfile(CottageOwner cottageOwner) throws Exception {
        CottageOwner forUpdate = this.findById(cottageOwner.getId());

        forUpdate.setCity(cottageOwner.getCity());
        forUpdate.setState(cottageOwner.getState());
        forUpdate.setResidence(cottageOwner.getResidence());
        forUpdate.setFirstName(cottageOwner.getFirstName());
        forUpdate.setLastName(cottageOwner.getLastName());
        forUpdate.setPhoneNumber(cottageOwner.getPhoneNumber());

        this.cottageOwnerRepository.save(forUpdate);
        return forUpdate;
    }


    @Override
    public CottageOwner updateBasicInfo(CottageOwner cottageOwner, CottageOwner forUpdate) {
        forUpdate.setCity(cottageOwner.getCity());
        forUpdate.setState(cottageOwner.getState());
        forUpdate.setResidence(cottageOwner.getResidence());
        forUpdate.setFirstName(cottageOwner.getFirstName());
        forUpdate.setLastName(cottageOwner.getLastName());

        return this.cottageOwnerRepository.save(forUpdate);
    }

}
