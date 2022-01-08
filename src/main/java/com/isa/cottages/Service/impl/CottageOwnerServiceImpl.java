package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.BoatOwner;
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

        forUpdate.setFirstName(cottageOwner.getFirstName());
        forUpdate.setLastName(cottageOwner.getLastName());
        forUpdate.setPhoneNumber(cottageOwner.getPhoneNumber());
        forUpdate.setResidence(cottageOwner.getResidence());
        forUpdate.setCity(cottageOwner.getCity());
        forUpdate.setState(cottageOwner.getState());

        this.cottageOwnerRepository.save(forUpdate);
        return forUpdate;
    }

    @Override
    public CottageOwner updateCottages(CottageOwner cottageOwner) throws Exception {
    CottageOwner forUpdate = this.findById(cottageOwner.getId());
    if(forUpdate == null) { throw new Exception("Cottage owner does not exist."); }

    forUpdate.setCottages(cottageOwner.getCottages());
    this.cottageOwnerRepository.save(forUpdate);
    return forUpdate;
    }

    @Override
    public CottageOwner defineUnavailability(CottageOwner cottageOwner) throws Exception {
        CottageOwner forUpdate = findById(cottageOwner.getId());

        forUpdate.setUnavailableFrom(cottageOwner.getUnavailableFrom());
        forUpdate.setUnavailableUntil(cottageOwner.getUnavailableUntil());

        this.cottageOwnerRepository.save(forUpdate);
        return forUpdate;
    }
}
