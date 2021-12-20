package com.isa.cottages.Controller;

import com.isa.cottages.Model.BoatOwner;
import com.isa.cottages.Service.impl.BoatOwnerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/boatOwner")
public class BoatOwnerController {

    private BoatOwnerServiceImpl boatOwnerService;

    @Autowired
    public BoatOwnerController(BoatOwnerServiceImpl boatOwnerService) {
        this.boatOwnerService = boatOwnerService;
    }


    @PreAuthorize("hasRole('BOAT_OWNER')")
    @GetMapping("/profile/{id}")
    public ModelAndView showProfile(Model model, @PathVariable("id") Long id) throws Exception {
        BoatOwner boatOwner = boatOwnerService.findById(id);
        model.addAttribute("principal", boatOwner);
        return new ModelAndView("boat/profile");
    }

    @PreAuthorize("hasRole('BOAT_OWNER')")
    @GetMapping("/profile/{id}/editProfile")
    public ModelAndView updateProfile(Model model, @PathVariable("id") Long id) throws Exception {
        BoatOwner boatOwner = this.boatOwnerService.findById(id);
        model.addAttribute("principal", boatOwner);
        return new ModelAndView("boat/editProfile");
    }

    @PreAuthorize("hasRole('BOAT_OWNER')")
    @PostMapping("/profile/{id}/editProfile/submit")
    public ModelAndView updateProfile(@PathVariable("id") Long id, Model model, @ModelAttribute BoatOwner boatOwner) {
        try {
            this.boatOwnerService.updateProfile(boatOwner);
            model.addAttribute("principal", boatOwner);
            return new ModelAndView("redirect:/boatOwner/profile/" + id.toString());
        } catch (Exception e) {
            return new ModelAndView("home");
        }
    }

}
