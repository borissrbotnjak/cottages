package com.isa.cottages.Controller;

import com.isa.cottages.Service.impl.CottageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/cottage")
public class CottageController {

    private CottageServiceImpl cottageService;

    @Autowired
    public CottageController(CottageServiceImpl cottageService){
        this.cottageService = cottageService;
    }
}
