package com.isa.cottages.Controller;

import com.isa.cottages.Service.impl.BoatOwnerServiceImpl;
import com.isa.cottages.Service.impl.CottageOwnerServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    private UserServiceImpl userService;
    private CottageOwnerServiceImpl cottageOwnerService;
    private BoatOwnerServiceImpl boatOwnerService;

    @Autowired
    public UserController(UserServiceImpl userService, CottageOwnerServiceImpl cottageOwnerService, BoatOwnerServiceImpl boatOwnerService){
        this.userService = userService;
        this.cottageOwnerService = cottageOwnerService;
        this.boatOwnerService = boatOwnerService;
    }
}
