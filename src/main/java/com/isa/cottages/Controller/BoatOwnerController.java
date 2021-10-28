package com.isa.cottages.Controller;

import com.isa.cottages.Service.impl.BoatOwnerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/boatOwner")
public class BoatOwnerController {

    private BoatOwnerServiceImpl boatOwnerService;

    @Autowired
    public BoatOwnerController(BoatOwnerServiceImpl boatOwnerService) {
        this.boatOwnerService = boatOwnerService;
    }
}
