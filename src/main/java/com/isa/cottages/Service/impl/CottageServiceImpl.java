package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.Cottage;
import com.isa.cottages.Model.CottageOwner;
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

    @Autowired
    public CottageServiceImpl(CottageRepository cottageRepository, UserServiceImpl userService,
                              CottageOwnerServiceImpl cottageOwnerService){
        this.cottageRepository = cottageRepository;
        this.userService = userService;
        this.cottageOwnerService = cottageOwnerService;
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
        c.setPromotionalDescription(cottage.getPromotionalDescription());
        c.setAdditionalServices(cottage.getAdditionalServices());
        c.setCottageOwner(cottage.getCottageOwner());
        c.setAvailableFrom(cottage.getAvailableFrom());
        c.setAvailableUntil(cottage.getAvailableUntil());

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
        forUpdate.setPromotionalDescription(cottage.getPromotionalDescription());
        forUpdate.setAdditionalServices(cottage.getAdditionalServices());
        forUpdate.setCottageOwner(cottage.getCottageOwner());
        forUpdate.setAvailableFrom(cottage.getAvailableFrom());
        forUpdate.setAvailableUntil(cottage.getAvailableUntil());

        this.cottageRepository.save(forUpdate);
        return forUpdate;
    }

    @Override
    public Cottage defineAvailability(Cottage cottage) throws Exception {
        Cottage forUpdate = findById(cottage.getId());

        forUpdate.setAvailableFrom(cottage.getAvailableFrom());
        forUpdate.setAvailableUntil(cottage.getAvailableUntil());

        this.cottageRepository.save(forUpdate);
        return forUpdate;
    }

    @Override
    public void removeCottage(Cottage cottage, Long oid) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) userService.findById(oid);
        if (cottageOwner == null) {
            throw new Exception("Cottage owner does not exist.");
        }
        Cottage c = findById(cottage.getId());

        Set<Cottage> cottages = cottageOwner.getCottages();
        cottages.remove(c);
        cottageOwner.setCottages(cottages);

        c.setCottageOwner(null);
//        this.cottageOwnerRepository.save(forUpdate);
        this.cottageOwnerService.updateCottages(cottageOwner);
        updateCottage(c);
    }

    @Override
    public Boolean canUpdateOrDelete(Long id) throws Exception {
        boolean updateOrDelete = true;
        Cottage cottage = findById(id);

        if (cottage.getReserved() == true) {
            updateOrDelete = false;
        }
            return updateOrDelete;
    }

    @Override
    public List<Cottage> orderByNameDesc() {
        return this.cottageRepository.findByOrderByNameDesc();
    }

    @Override
    public List<Cottage> orderByNameAsc() {
        return this.cottageRepository.findByOrderByNameAsc();
    }

    @Override
    public List<Cottage> orderByRatingAsc() {
        return this.cottageRepository.findByOrderByAverageRatingAsc();
    }
    @Override
    public List<Cottage> orderByRatingDesc() {
        return this.cottageRepository.findByOrderByAverageRatingDesc();
    }

    @Override
    public List<Cottage> orderByAddressDesc() {
        return this.cottageRepository.findByOrderByResidenceDescCityDescStateDesc();
    }

    @Override
    public List<Cottage> orderByAddressAsc() {
        return this.cottageRepository.findByOrderByResidenceAscCityAscStateAsc();
    }

}

