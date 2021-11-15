package com.isa.cottages.Controller;

import com.isa.cottages.Model.Cottage;
import com.isa.cottages.Model.CottageOwner;
import com.isa.cottages.Model.CottageReservation;
import com.isa.cottages.Service.impl.CottageReservationServiceImpl;
import com.isa.cottages.Service.impl.CottageServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@RestController
@RequestMapping("/cottageReservations")
public class CottageReservationController {

    private CottageReservationServiceImpl reservationService;
    private UserServiceImpl userService;
    private CottageServiceImpl cottageService;

    @Autowired
    public CottageReservationController(CottageReservationServiceImpl reservationService,
                                        UserServiceImpl userService,
                                        CottageServiceImpl cottageService) {
        this.reservationService = reservationService;
        this.userService = userService;
        this.cottageService = cottageService;
    }

    @GetMapping("/allActionsByCottage/{id}")
    public ModelAndView getActionsByCottage(@PathVariable Long id, Model model) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        Cottage cottage = this.cottageService.findById(id);
        model.addAttribute("cottage", cottage);
        if(cottage == null) {
            throw new Exception("Cottage with this id does not exist.");
        }
        model.addAttribute("cottageReservations", reservationService.findByCottage(id));

        return new ModelAndView("allActionsByCottage");
    }

//    @GetMapping("/reservedCottages/{id}")
//    @PreAuthorize("hasRole('COTTAGE_OWNER')")
//    public ModelAndView getReservedCottages(@PathVariable Long id, Model model) throws Exception {
//        List<Reservation> reserved = this.reservationService.findReserved(id);
//        model.addAttribute("reservations", reserved);
//        model.addAttribute("principal", this.userService.getUserFromPrincipal());
//
//        return new ModelAndView("reservedCottages");
//    }

    @GetMapping("/upcomingReservations")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView showUpcomingReservations(Model model) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);
        model.addAttribute("reservations", this.reservationService.getUpcomingReservations());

        return new ModelAndView("upcomingCottageReservations");
    }

    @GetMapping("/reservationHistory")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView showReservationHistory(Model model) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);
        model.addAttribute("reservations", this.reservationService.getPastReservations());

        return new ModelAndView("cottageReservationHistory");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/{id}/defineAction")
    public ModelAndView defineAction(@PathVariable Long id, Model model) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        model.addAttribute("cottage", this.cottageService.findById(id));
        CottageReservation cottageReservation = new CottageReservation();
        model.addAttribute("cottageReservation", cottageReservation);

        Collection<CottageReservation> cottageReservations = this.reservationService.findByCottage(id);
        model.addAttribute("cottageReservations", cottageReservations);

        return new ModelAndView("defineAction");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @PostMapping("/{id}/defineAction/submit")
    public ModelAndView defineAction(@PathVariable Long id, @ModelAttribute CottageReservation cottageReservation, Model model) throws Exception {
//        if (this.cottageService.findById(cottage.getId()) != null) {
//            throw new ResourceConflictException(cottage.getId(), "Cottage with this id already exist.");
//        }
        Collection<CottageReservation> cottageReservations = this.reservationService.findByCottage(id);
        model.addAttribute("cottageReservations", cottageReservations);

        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        cottageReservation.setCottageOwner((CottageOwner) this.userService.getUserFromPrincipal());
        cottageReservation.setCottage((Cottage) this.cottageService.findById(id));
        this.reservationService.saveAction(cottageReservation);
        return new ModelAndView("redirect:/cottages/{id}/");
    }
}
