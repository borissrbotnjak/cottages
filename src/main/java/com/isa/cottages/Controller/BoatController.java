package com.isa.cottages.Controller;

import com.isa.cottages.Exception.ResourceConflictException;
import com.isa.cottages.Model.Boat;
import com.isa.cottages.Service.impl.BoatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/boat")
public class BoatController {

    private BoatServiceImpl boatService;

    @Autowired
    public BoatController(BoatServiceImpl boatService){
        this.boatService = boatService;
    }

    @GetMapping(value = "/addBoat")
    @PreAuthorize("hasRole('BOAT_OWNER')")
    public ModelAndView addBoatForm(Model model){
        Boat boat = new Boat();
        model.addAttribute("boat", boat);
        return new ModelAndView("addBoatForm");
    }

    @PostMapping(value = "/addBoat/submit")
    @PreAuthorize("hasRole('BOAT_OWNER')")
    public ModelAndView addBoat(@ModelAttribute Boat boat) throws Exception {
        if(this.boatService.findById(boat.getId()) != null) {
            throw new ResourceConflictException(boat.getId(), "Boat with this id already exist");
        }
        this.boatService.saveBoat(boat);
        return new ModelAndView("redirect:/boats/");
    }
}
