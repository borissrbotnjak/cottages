package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.Boat;
import com.isa.cottages.Model.BoatOwner;
import com.isa.cottages.Model.NavigationEquipment;
import com.isa.cottages.Repository.NavigationEquipmentRepository;
import com.isa.cottages.Service.NavigationEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NavigationEquipmentServiceImpl implements NavigationEquipmentService {

    private NavigationEquipmentRepository navigationEquipmentRepository;
    private BoatServiceImpl boatService;
    private UserServiceImpl userService;

    @Autowired
    public NavigationEquipmentServiceImpl (NavigationEquipmentRepository navigationEquipmentRepository,
                                           BoatServiceImpl boatService, UserServiceImpl userService) {
        this.navigationEquipmentRepository = navigationEquipmentRepository;
        this.boatService = boatService;
        this.userService = userService;
    }


    @Override
    public List<NavigationEquipment> findByBoat(Long id) throws Exception {
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();
        Boat boat = boatService.findById(id);
        List<NavigationEquipment> all = navigationEquipmentRepository.findByBoat(id);
        List<NavigationEquipment> myNavigationEquipments = new ArrayList<NavigationEquipment>();

        for (NavigationEquipment as:all) {
            if(Objects.equals(as.getBoat().getId(), boat.getId())) {
                myNavigationEquipments.add(as);
            }
        }
        return myNavigationEquipments;
    }

    @Override
    public NavigationEquipment save(NavigationEquipment navigationEquipment) throws Exception {
        NavigationEquipment n = new NavigationEquipment();

        n.setName(navigationEquipment.getName());
        n.setBoat(navigationEquipment.getBoat());

        this.navigationEquipmentRepository.save(n);

        return n;
    }


}
