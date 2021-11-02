package com.isa.cottages.Controller;

import com.isa.cottages.Model.CottageOwner;
import com.isa.cottages.Service.impl.CottageOwnerServiceImpl;
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

    @Autowired
    public CottageOwnerController (CottageOwnerServiceImpl cottageOwnerService){
        this.cottageOwnerService = cottageOwnerService;
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/profile/{id}")
    public ModelAndView showProfile(Model model, @PathVariable("id") Long id) throws Exception{
        CottageOwner cottageOwner = cottageOwnerService.findById(id);
//        if(cottageOwner == null){
//            throw new Exception("User does not exist.");
//        }
        model.addAttribute("user", cottageOwner);
        return new ModelAndView("profileCottageOwner");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/profile/{id}/editProfile")
    public ModelAndView updateProfile(Model model, @PathVariable("id") Long id) throws Exception{
        CottageOwner cottageOwner = this.cottageOwnerService.findById(id);
        if(cottageOwner == null) {
            throw new Exception("User with this id does not exist.");
        }
        model.addAttribute("user", cottageOwner);
        return new ModelAndView("editOwnerProfile");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @PostMapping("/profile/{id}/editProfile/submit")
    public ModelAndView updateProfile(@ModelAttribute CottageOwner cottageOwner, Model model,
                                      @PathVariable("id") Long id) throws Exception {
        try{
            this.cottageOwnerService.updateProfile(cottageOwner);
            model.addAttribute("user", cottageOwner);
            return new ModelAndView("redirect:/cottageOwner/profile" + id.toString());
        } catch (Exception e) {
            return new ModelAndView("home");
        }
    }
}
