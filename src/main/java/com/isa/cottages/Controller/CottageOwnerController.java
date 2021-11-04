package com.isa.cottages.Controller;

import com.isa.cottages.Exception.ResourceConflictException;
import com.isa.cottages.Model.Cottage;
import com.isa.cottages.Model.CottageOwner;
import com.isa.cottages.Service.impl.CottageOwnerServiceImpl;
import com.isa.cottages.Service.impl.CottageServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/cottageOwner")
public class CottageOwnerController {

    private CottageOwnerServiceImpl cottageOwnerService;

    private CottageServiceImpl cottageService;

    private UserServiceImpl userService;

    @Autowired
    public CottageOwnerController (CottageOwnerServiceImpl cottageOwnerService, CottageServiceImpl cottageService, UserServiceImpl userService){
        this.cottageOwnerService = cottageOwnerService;
        this.cottageService = cottageService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/profile/{id}")
    public ModelAndView showProfile(Model model, @PathVariable("id") Long id) throws Exception {
        CottageOwner cottageOwner = cottageOwnerService.findById(id);
        model.addAttribute("user", cottageOwner);
        return new ModelAndView("profileCottageOwner");
    }
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/profile/{id}/editProfile")
    public ModelAndView updateProfile(Model model, @PathVariable("id") Long id) throws Exception {
        CottageOwner cottageOwner = this.cottageOwnerService.findById(id);
        model.addAttribute("user", cottageOwner);
        return new ModelAndView("editOwnerProfile");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @PostMapping("/profile/{id}/editProfile/submit")
    public ModelAndView updateProfile(@PathVariable("id") Long id, Model model, @ModelAttribute CottageOwner cottageOwner) {
        try {
            this.cottageOwnerService.updateProfile(cottageOwner);
            model.addAttribute("user", cottageOwner);
            return new ModelAndView("redirect:/cottageOwner/profile/" + id.toString());
        } catch (Exception e) {
            return new ModelAndView("home");
        }
    }
}
