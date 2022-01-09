package com.isa.cottages.Service;

import com.isa.cottages.Model.FishingEquipment;

import java.util.List;

public interface FishingEquipmentService {

    List<FishingEquipment> findByBoat(Long id) throws Exception;

    FishingEquipment save (FishingEquipment fishingEquipment) throws Exception;
}
