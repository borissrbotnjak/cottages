package com.isa.cottages.Controller;

import com.isa.cottages.Exception.ResourceConflictException;
import com.isa.cottages.Model.Cottage;
import com.isa.cottages.Service.UserService;
import com.isa.cottages.Service.impl.CottageServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/cottage")
public class CottageController {

    private CottageServiceImpl cottageService;

    private UserServiceImpl userService;

    @Autowired
    public CottageController(CottageServiceImpl cottageService, UserServiceImpl userService){
        this.cottageService = cottageService;
        this.userService = userService;
    }

    @GetMapping(value = "/addCottage")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView addCottageForm(Model model){
        Cottage cottage = new Cottage();
        model.addAttribute("cottage", cottage);
        List<Cottage> cottages = this.cottageService.findAll();
        model.addAttribute("cottages", cottages);
        return new ModelAndView("addCottageForm");
    }

    @PostMapping(value = "/addCottage/submit")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView addCottage(@ModelAttribute Cottage cottage) throws Exception {
        if (this.cottageService.findById(cottage.getId()) != null) {
            throw new ResourceConflictException(cottage.getId(), "Cottage with this id already exist.");
        }
        this.cottageService.saveCottage(cottage);
        return new ModelAndView("redirect:/allMyCottages/");
    }

//    @GetMapping("/allMyCottages")
//    @PreAuthorize("hasRole('COTTAGE_OWNER')")
//    public ModelAndView getAllMyCottages(Model model, @PathVariable Long id) throws Exception{
//        List<Cottage> cottages = this.cottageService.findAllByCottageOwner(id);
//        model.addAttribute("cottages", cottages);
//        model.addAttribute("principal", this.userService.getUserFromPrincipal());
//
//        return new ModelAndView("/allMyCottages");
//    }
}
