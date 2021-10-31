package com.isa.cottages.Controller;

import com.isa.cottages.Exception.ResourceConflictException;
import com.isa.cottages.Model.Cottage;
import com.isa.cottages.Service.impl.CottageServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
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
@RequestMapping(value = "/cottage")
public class CottageController {

    private CottageServiceImpl cottageService;
    private UserServiceImpl userService;

    @Autowired
    public CottageController(CottageServiceImpl cottageService){
        this.cottageService = cottageService;
    }

    @GetMapping(value = "/addCottage")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView addCottageForm(Model model){
        Cottage cottage = new Cottage();
        model.addAttribute("cottage", cottage);
        return new ModelAndView("addCottageForm");
    }

    @PostMapping(value = "/addCottage/submit")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView addCottage(@ModelAttribute Cottage cottage) throws Exception {
        if (this.cottageService.findById(cottage.getId()) != null) {
            throw new ResourceConflictException(cottage.getId(), "Cottage with this id already exist.");
        }
        this.cottageService.saveCottage(cottage);
        return new ModelAndView("redirect:/cottages/");
    }

    @GetMapping("/allCottages")
    public ModelAndView getAllCottages(Model model, String keyword) throws Exception {
        if (keyword != null) {
            model.addAttribute("cottages", this.cottageService.findByKeyword(keyword));
        } else {
            model.addAttribute("pharmacies", this.cottageService.findAll());
        }

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("cottages");
        } catch (Exception e) {
            return new ModelAndView("cottagesGuests");
        }
    }
}
