package com.isa.cottages.Controller;

import com.isa.cottages.Model.Cottage;
import com.isa.cottages.Model.CottageOwner;
import com.isa.cottages.Service.impl.CottageOwnerServiceImpl;
import com.isa.cottages.Service.impl.CottageServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Controller
@RequestMapping(value = "/cottages")
public class CottageController {

    private CottageServiceImpl cottageService;
    private UserServiceImpl userService;
    private CottageOwnerServiceImpl cottageOwnerService;

    @Autowired
    public CottageController(CottageServiceImpl cottageService, UserServiceImpl userService, CottageOwnerServiceImpl cottageOwnerService) {
        this.cottageService = cottageService;
        this.userService = userService;
        this.cottageOwnerService = cottageOwnerService;
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/addCottage/{id}")
    public ModelAndView addCottageForm(@PathVariable Long id, Model model) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);
        Cottage cottage = new Cottage();
        model.addAttribute("cottage", cottage);

        Collection<Cottage> cottages = this.cottageService.findByCottageOwner(id);
        model.addAttribute("cottages", cottages);

        return new ModelAndView("addCottageForm");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @PostMapping("/addCottage/{id}/submit")
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

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/allMyCottages/{id}")
    public ModelAndView getAllMyCottages (@PathVariable Long id, Model model, String keyword) throws Exception{
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);
//        Cottage cottage = new Cottage();
//        model.addAttribute("cottage", cottage);
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

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/{id}/edit")
    public ModelAndView edit(Model model, @PathVariable Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        Cottage cottage = this.cottageService.findById(id);
        model.addAttribute("cottage", cottage);

        Collection<Cottage> cottages = this.cottageService.findByCottageOwner(id);
        model.addAttribute("cottages", cottages);
        return new ModelAndView("editCottage");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @PostMapping("/{id}/edit/submit")
    public ModelAndView edit(@PathVariable Long id, Model model, Cottage cottage) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        Collection<Cottage> cottages = this.cottageService.findByCottageOwner(id);
        model.addAttribute("cottages", cottages);
//        model.addAttribute("cottage", cottage);

        cottage.setCottageOwner((CottageOwner) this.userService.getUserFromPrincipal());
        this.cottageService.updateCottage(cottage);
        return new ModelAndView("redirect:/cottages/{id}/");
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

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/allMyCottages/{id}/editCottage/{cid}")
    public ModelAndView updateCottage(Model model, @PathVariable("id") Long id,
                                      @PathVariable("cid") Long cid) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        Cottage cottage = this.cottageService.findById(cid);
        model.addAttribute("cottage", cottage);

        Collection<Cottage> cottages = this.cottageService.findByCottageOwner(id);
        model.addAttribute("cottages", cottages);
        return new ModelAndView("editCottage");
    }

//    @PreAuthorize("hasRole('COTTAGE_OWNER')")
//    @PostMapping("/allMyCottages/{id}/editCottage/{cid}/submit")
//    public ModelAndView updateCottage(Model model, @PathVariable("id") Long id,
//                                      @PathVariable("cid") Long cid,
//                                      @ModelAttribute Cottage cottage) throws Exception{
//        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
//        model.addAttribute("principal", cottageOwner);
//        Collection<Cottage> cottages = this.cottageService.findByCottageOwner(id);
//        model.addAttribute("cottages", cottages);
//
//        this.cottageService.findById(cid);
//        cottage.setCottageOwner((CottageOwner) this.userService.getUserFromPrincipal());
//        this.cottageService.updateCottage(cottage);
//        model.addAttribute("cottage", cottage);
//        return new ModelAndView("redirect:/cottages/allMyCottages/{id}");
//    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping(value = "/allMyCottages/{id}/remove/{cid}")
    public ModelAndView removeCottage(@PathVariable Long cid,
                                      @PathVariable Long id,
                                      Model model) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        Cottage cottage = this.cottageService.findById(cid);
//        cottage.setCottageOwner((CottageOwner) this.userService.getUserFromPrincipal());
        this.cottageService.removeCottage(cottage, id);
        return new ModelAndView("redirect:/cottages/allMyCottages/{id}" );
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/{id}/defineAvailability")
    public ModelAndView defineAvailability(Model model, @PathVariable Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        Cottage cottage = this.cottageService.findById(id);
        model.addAttribute("cottage", cottage);

        return new ModelAndView("defineAvailability");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @PostMapping("/{id}/defineAvailability/submit")
    public ModelAndView defineAvailability(Model model, @PathVariable Long id, @ModelAttribute Cottage cottage,
                                           @RequestParam String availableFrom,
                                           @RequestParam String availableUntil) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        Collection<Cottage> cottages = this.cottageService.findByCottageOwner(id);
        model.addAttribute("cottages", cottages);
        model.addAttribute("cottage", cottage);

        cottage.setCottageOwner((CottageOwner) this.userService.getUserFromPrincipal());
        this.cottageService.defineAvailability(cottage);

        return new ModelAndView("redirect:/cottages/{id}/");
    }
}
