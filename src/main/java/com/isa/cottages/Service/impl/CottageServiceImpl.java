package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.Cottage;
import com.isa.cottages.Model.CottageOwner;
import com.isa.cottages.Repository.CottageOwnerRepository;
import com.isa.cottages.Repository.CottageRepository;
import com.isa.cottages.Service.CottageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CottageServiceImpl implements CottageService {

    private CottageRepository cottageRepository;
    private UserServiceImpl userService;
    private CottageOwnerServiceImpl cottageOwnerService;
    private CottageOwnerRepository cottageOwnerRepository;

    @Autowired
    public CottageServiceImpl(CottageRepository cottageRepository, UserServiceImpl userService,
                              CottageOwnerServiceImpl cottageOwnerService,
                              CottageOwnerRepository cottageOwnerRepository){
        this.cottageRepository = cottageRepository;
        this.userService = userService;
        this.cottageOwnerService = cottageOwnerService;
        this.cottageOwnerRepository = cottageOwnerRepository;
    }

    @Override
    public Cottage findById(Long id) throws Exception {
        if(this.cottageRepository.findById(id).isEmpty()) {
            throw new Exception("No such value(cottage service)");
        }
        return this.cottageRepository.findById(id).get();
    }

    @Override
    public Cottage saveCottage(Cottage cottage) throws Exception {
        Cottage c = new Cottage();
        c.setName(cottage.getName());
        c.setResidence(cottage.getResidence());
        c.setCity(cottage.getCity());
        c.setState(cottage.getState());
        c.setNumberOfRooms(cottage.getNumberOfRooms());
        c.setNumberOfBeds(cottage.getNumberOfBeds());
        c.setRules(cottage.getRules());
        c.setDescription(cottage.getDescription());
        c.setCottageOwner(cottage.getCottageOwner());
        this.cottageRepository.save(c);

        return c;
    }

    @Override
    public Collection<Cottage> findAll() {
        return this.cottageRepository.findAll();
    }

    @Override
    public List<Cottage> findByCottageOwner(Long id) throws Exception{
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        List<Cottage> all = this.cottageRepository.findByCottageOwner(id);
        List<Cottage> myCottages = new ArrayList<Cottage>();

        for (Cottage co:all) {
            if(Objects.equals(co.getCottageOwner().getId(), cottageOwner.getId())) {
                myCottages.add(co);
            }
        }
        return myCottages;
    }

    @Override
    public List<Cottage> findByKeyword(String keyword) {
        return this.cottageRepository.findByKeyword(keyword);
    }

    public List<Cottage> findByKeywordAndCottageOwner(String keyword, Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        List<Cottage> all = this.cottageRepository.findByKeywordAndCottageOwner(keyword, id);
        List<Cottage> myCottages = new ArrayList<Cottage>();

        for (Cottage co:all) {
            if(Objects.equals(co.getCottageOwner().getId(), cottageOwner.getId())) {
                myCottages.add(co);
            }
        }
        return myCottages;
    }

    @Override
    public Cottage updateCottage(Cottage cottage) throws Exception {
        Cottage forUpdate = findById(cottage.getId());

        forUpdate.setName(cottage.getName());
        forUpdate.setResidence(cottage.getResidence());
        forUpdate.setCity(cottage.getCity());
        forUpdate.setState(cottage.getState());
        forUpdate.setNumberOfRooms(cottage.getNumberOfRooms());
        forUpdate.setNumberOfBeds(cottage.getNumberOfBeds());
        forUpdate.setRules(cottage.getRules());
        forUpdate.setDescription(cottage.getDescription());
        forUpdate.setCottageOwner(cottage.getCottageOwner());

        this.cottageRepository.save(forUpdate);
        return forUpdate;
    }

    @Override
    public CottageOwner removeCottage(Cottage cottage, CottageOwner cottageOwner) throws Exception {
        CottageOwner forUpdate = (CottageOwner) userService.findById(cottageOwner.getId());
//        if (cottageOwner == null) {
//            throw new Exception("Cottage owner does not exist.");
//        }
//        Cottage cot = findById(cottage.getId());
        Set<Cottage> cottages = forUpdate.getCottages();
//        cottages.remove(cot);
        cottages.remove(cottage);
        forUpdate.setCottages(cottages);
        this.cottageOwnerRepository.save(forUpdate);
        this.cottageOwnerService.updateCottages(cottageOwner);
        updateCottage(cottage);
        return forUpdate;
    }

}

