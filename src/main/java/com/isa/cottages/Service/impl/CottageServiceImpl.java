package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.Cottage;
import com.isa.cottages.Repository.CottageRepository;
import com.isa.cottages.Service.CottageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class CottageServiceImpl implements CottageService {

    private CottageRepository cottageRepository;
    private UserServiceImpl userService;

    @Autowired
    public CottageServiceImpl(CottageRepository cottageRepository, UserServiceImpl userService){
        this.cottageRepository = cottageRepository;
        this.userService = userService;
    }

    @Override
    public Cottage findById(Long id) throws Exception {
        if(!this.cottageRepository.findById(id).isPresent()) {
            throw new Exception("No such value(cottage service)");
        }
        return this.cottageRepository.findById(id).get();
    }

//    @Override
//    public List<Cottage> findAll() {
//        return this.cottageRepository.findAll();
//    }

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

//    @Override
//    public List<Cottage> findByCottageOwner(Long id) {
//        List<Cottage> all = this.cottageRepository.findAllByCottageOwner(id);
//        List<Cottage> my = new ArrayList<>();
//
//        for(Cottage cot: all){
//            my.add(cot);
//        }
//        return my;
//    }

    @Override
    public Collection<Cottage> findAll() {
        return this.cottageRepository.findAll();
    }

    @Override
    public List<Cottage> findByKeyword(String keyword) {
        return this.cottageRepository.findByKeyword(keyword);
    }

    @Override
    public Cottage updateCottage(Cottage cottage) throws Exception {
        Cottage forUpdate = findById(cottage.getId());

        forUpdate.setName(forUpdate.getName());
        forUpdate.setResidence(forUpdate.getResidence());
        forUpdate.setCity(forUpdate.getCity());
        forUpdate.setState(forUpdate.getState());
        forUpdate.setNumberOfRooms(forUpdate.getNumberOfRooms());
        forUpdate.setNumberOfBeds(forUpdate.getNumberOfBeds());
        forUpdate.setRules(forUpdate.getRules());
        forUpdate.setDescription(forUpdate.getDescription());

        this.cottageRepository.save(forUpdate);
        return forUpdate;
    }

    @Override
    public Cottage removeCottage(Cottage cottage) throws Exception {
        Cottage forDelete = findById(cottage.getId());
//        Set<Cottage> cottages = forUpdate.getCottageOwner().getCottages();
        List<Cottage> cottages = (List<Cottage>) this.cottageRepository.findAll();
        cottages.remove(forDelete);
        this.cottageRepository.save(forDelete);
        return forDelete;
    }

}

