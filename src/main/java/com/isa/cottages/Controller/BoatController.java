package com.isa.cottages.Controller;

import com.isa.cottages.Service.impl.BoatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/boat")
public class BoatController {

    private BoatServiceImpl boatService;

    @Autowired
    public BoatController(BoatServiceImpl boatService){
        this.boatService = boatService;
    }
}
