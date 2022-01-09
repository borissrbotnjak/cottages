package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.AdditionalService;
import com.isa.cottages.Model.Boat;
import com.isa.cottages.Model.BoatOwner;
import com.isa.cottages.Model.FishingEquipment;
import com.isa.cottages.Repository.FishingEquipmentRepository;
import com.isa.cottages.Service.FishingEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FishingEquipmentServiceImpl implements FishingEquipmentService {

    private FishingEquipmentRepository fishingEquipmentRepository;
    private UserServiceImpl userService;
    private BoatServiceImpl boatService;

    @Autowired
    public FishingEquipmentServiceImpl (FishingEquipmentRepository fishingEquipmentRepository,
                                         UserServiceImpl userService,
                                         BoatServiceImpl boatService) {
        this.fishingEquipmentRepository = fishingEquipmentRepository;
        this.userService = userService;
        this.boatService = boatService;
    }

    @Override
    public List<FishingEquipment> findByBoat(Long id) throws Exception{
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();
        Boat boat = boatService.findById(id);
        List<FishingEquipment> all = this.fishingEquipmentRepository.findByBoat(id);
        List<FishingEquipment> myFishingEquipments = new ArrayList<FishingEquipment>();

        for (FishingEquipment fe:all) {
            if(Objects.equals(fe.getBoat().getId(), boat.getId())) {
                myFishingEquipments.add(fe);
            }
        }
        return myFishingEquipments;
    }

    @Override
    public FishingEquipment save(FishingEquipment fishingEquipment) throws Exception {
        FishingEquipment fe = new FishingEquipment();

        fe.setName(fishingEquipment.getName());
        fe.setBoat(fishingEquipment.getBoat());

        this.fishingEquipmentRepository.save(fe);

        return fe;
    }
}
