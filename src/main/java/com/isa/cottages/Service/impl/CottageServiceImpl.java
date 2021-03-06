package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.Cottage;
import com.isa.cottages.Repository.CottageRepository;
import com.isa.cottages.Service.CottageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CottageServiceImpl implements CottageService {

    private CottageRepository cottageRepository;

    @Autowired
    public CottageServiceImpl(CottageRepository cottageRepository){
        this.cottageRepository = cottageRepository;
    }

    @Override
    public Cottage findById(Long id) throws Exception {
        if(!this.cottageRepository.findById(id).isPresent()) {
            throw new Exception("No such value(cottage service)");
        }
        return this.cottageRepository.findById(id).get();
    }

    @Override
    public Cottage saveCottage(Cottage cottage) {
        Cottage c = new Cottage();
        c.setName(cottage.getName());
        c.setResidence(cottage.getResidence());
        c.setCity(cottage.getCity());
        c.setState(cottage.getState());
        c.setNumberOfRooms(cottage.getNumberOfRooms());
        c.setNumberOfBeds(cottage.getNumberOfBeds());
        c.setRules(cottage.getRules());
        c.setDescription(cottage.getDescription());
        this.cottageRepository.save(c);
        return c;
    }
}
