package com.isa.cottages.Controller;

import com.isa.cottages.Model.Cottage;
import com.isa.cottages.Model.CottageOwner;
import com.isa.cottages.Repository.CottageRepository;
import com.isa.cottages.Service.impl.CottageOwnerServiceImpl;
import com.isa.cottages.Service.impl.CottageServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
@RequestMapping(value = "/cottages")
public class CottageController {

    private CottageRepository cottageRepository;
    private CottageServiceImpl cottageService;

    private UserServiceImpl userService;

    private CottageOwnerServiceImpl cottageOwnerService;

    @Autowired
    public CottageController(CottageServiceImpl cottageService, UserServiceImpl userService, CottageOwnerServiceImpl cottageOwnerService) {
        this.cottageService = cottageService;
        this.userService = userService;
        this.cottageOwnerService = cottageOwnerService;
    }

    @GetMapping("/addCottage/{id}")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView addCottageForm(@PathVariable Long id, Model model) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);
        Cottage cottage = new Cottage();
        model.addAttribute("cottage", cottage);

        Collection<Cottage> cottages = this.cottageService.findByCottageOwner(id);
        model.addAttribute("cottages", cottages);

        return new ModelAndView("addCottageForm");
    }

    @PostMapping("/addCottage/{id}/submit")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView addCottage(@PathVariable Long id, @ModelAttribute Cottage cottage, Model model) throws Exception {
//        if (this.cottageService.findById(cottage.getId()) != null) {
//            throw new ResourceConflictException(cottage.getId(), "Cottage with this id already exist.");
//        }
        Collection<Cottage> cottages = this.cottageService.findByCottageOwner(id);
        model.addAttribute("cottages", cottages);

        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        cottage.setCottageOwner((CottageOwner) this.userService.getUserFromPrincipal());
        this.cottageService.saveCottage(cottage);
        return new ModelAndView("redirect:/cottages/allMyCottages/{id}/");
    }

    @GetMapping("/allMyCottages/{id}")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView getAllMyCottages (@PathVariable Long id, Model model, String keyword) throws Exception{
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        Cottage cottage = new Cottage();
        model.addAttribute("cottage", cottage);

        if(cottageOwner == null) {
            throw new Exception("Cottage owner does not exist.");
        }
        if (keyword != null) {
            model.addAttribute("cottages", this.cottageService.findByKeyword(keyword));
        } else {
            model.addAttribute("cottages", cottageService.findByCottageOwner(id));
        }
        return new ModelAndView("allMyCottages");
    }

    @GetMapping("/{id}")
    public ModelAndView showCottage(@PathVariable("id") Long id, Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("cottage", this.cottageService.findById(id));
        return new ModelAndView("cottage");
    }

    @GetMapping("/allCottages")
    public ModelAndView getAllCottages(Model model, String keyword) throws Exception {
        if (keyword != null) {
            model.addAttribute("cottages", this.cottageService.findByKeyword(keyword));
        } else {
            model.addAttribute("cottages", this.cottageService.findAll());
        }

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("cottages");
        } catch (Exception e) {
            //System.out.println("error all cottages");
            return new ModelAndView("cottagesGuests");
        }
    }

    @GetMapping("/allMyCottages/{id}/editCottage/{cid}")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView editCottageForm(Model model, @PathVariable("id") Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        Cottage cottage = this.cottageService.findById(id);
        model.addAttribute("cottage", cottage);

        Collection<Cottage> cottages = this.cottageService.findByCottageOwner(id);
        model.addAttribute("cottages", cottages);
        return new ModelAndView("editCottage");
    }

    @PostMapping("/allMyCottages/{id}/editCottage/{cid}/submit")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView updateCottage(@PathVariable("id") Long id, Model model) throws Exception{
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        Collection<Cottage> cottages = this.cottageService.findByCottageOwner(id);
        model.addAttribute("cottages", cottages);

        Cottage cottage = this.cottageService.findById(id);
        cottage.setCottageOwner((CottageOwner) this.userService.getUserFromPrincipal());
        this.cottageService.updateCottage(cottage);
        return new ModelAndView("redirect:/cottages/allMyCottages/{id}/");
    }

    @GetMapping("/allMyCottages/remove/{id}")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView removeCottage(@PathVariable("id") Long id, Model model) throws Exception{
//        CottageOwner cottageOwner = (CottageOwner) this.userService.findById(id);
        Cottage cottage = this.cottageService.findById(id);

        Cottage c = this.cottageService.removeCottage(cottage);
        return new ModelAndView("redirect:/cottages/allMyCottages/{id}");
    }
}
