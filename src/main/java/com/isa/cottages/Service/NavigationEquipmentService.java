package com.isa.cottages.Service;

import com.isa.cottages.Model.NavigationEquipment;

import java.util.List;

public interface NavigationEquipmentService {

    List<NavigationEquipment> findByBoat(Long id) throws Exception;

    NavigationEquipment save(NavigationEquipment navigationEquipment) throws Exception;
}
