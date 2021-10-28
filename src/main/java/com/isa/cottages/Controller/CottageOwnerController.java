package com.isa.cottages.Controller;

import com.isa.cottages.Service.impl.CottageOwnerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/cottageOwner")
public class CottageOwnerController {

    private CottageOwnerServiceImpl cottageOwnerService;

    @Autowired
    public CottageOwnerController (CottageOwnerServiceImpl cottageOwnerService){
        this.cottageOwnerService = cottageOwnerService;
    }
}
