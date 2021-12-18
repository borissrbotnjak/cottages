package com.isa.cottages.Controller;

import com.isa.cottages.Exception.ResourceConflictException;
import com.isa.cottages.Model.Boat;
import com.isa.cottages.Model.BoatOwner;
import com.isa.cottages.Service.impl.BoatServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/boats")
public class BoatController {

    private BoatServiceImpl boatService;
    private UserServiceImpl userService;

    @Autowired
    public BoatController(BoatServiceImpl boatService, UserServiceImpl userService){
        this.boatService = boatService;
        this.userService = userService;
    }

    @GetMapping("/allBoats")
    public ModelAndView getAllBoats(Model model, String keyword) throws Exception {
        if (keyword != null) {
            model.addAttribute("boats", this.boatService.findByKeyword(keyword));
        } else {
            model.addAttribute("boats", this.boatService.getAll());
        }


        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("boat/boats");
        } catch (Exception e) {
            System.out.println("error all boats");
            return new ModelAndView("home");
        }
    }

    @PreAuthorize("hasRole('BOAT_OWNER')")
    @GetMapping("/allMyBoats/{id}")
    public ModelAndView getAllMyBoats (@PathVariable Long id, Model model, String keyword) throws Exception{
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", boatOwner);
        if(boatOwner == null) {
            throw new Exception("Boat owner does not exist.");
        }
        if (keyword != null) {
            model.addAttribute("boats", this.boatService.findByKeyword(keyword));
        } else {
            model.addAttribute("boats", boatService.findByBoatOwner(id));
        }
        return new ModelAndView("boat/allMyBoats");
    }

    @GetMapping("/{id}")
    public ModelAndView showBoat(@PathVariable("id") Long id, Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("boat", this.boatService.findById(id));
        return new ModelAndView("boat/boat");
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


    @GetMapping("/allBoats/sortByNameDesc")
    public ModelAndView sortByNameDesc(Model model) {
        List<Boat> sorted = this.boatService.orderByNameDesc();
        model.addAttribute("boats", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("boat/boats");
        } catch (Exception e) {
            return new ModelAndView("home");
        }
    }

    @GetMapping("/allBoats/sortByNameAsc")
    public ModelAndView sortByNameAsc(Model model) {
        List<Boat> sorted = this.boatService.orderByNameAsc();
        model.addAttribute("boats", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("boat/boats");
        } catch (Exception e) {
            return new ModelAndView("home");
        }
    }

    @GetMapping("/allBoats/sortByRatingDesc")
    public ModelAndView sortByRatingDesc(Model model) {
        List<Boat> sorted = this.boatService.orderByRatingDesc();
        model.addAttribute("boats", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("boat/boats");
        } catch (Exception e) {
            return new ModelAndView("home");
        }
    }

    @GetMapping("/allBoats/sortByRatingAsc")
    public ModelAndView sortByRatingAsc(Model model) {
        List<Boat> sorted = this.boatService.orderByRatingAsc();
        model.addAttribute("boats", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("boat/boats");
        } catch (Exception e) {
            return new ModelAndView("home");
        }
    }

    @GetMapping("/allBoats/SortByAddressAsc")
    public ModelAndView orderByAddressAsc(Model model) {
        List<Boat> sorted = this.boatService.orderByAddressAsc();
        model.addAttribute("boats", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("boat/boats");
        } catch (Exception e) {
            return new ModelAndView("home");
        }
    }

    @GetMapping("/allBoats/SortByAddressDesc")
    public ModelAndView orderByAddressDesc(Model model) {
        List<Boat> sorted = this.boatService.orderByAddressDesc();
        model.addAttribute("boats", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("boat/boats");
        } catch (Exception e) {
            return new ModelAndView("home");
        }
    }

}
