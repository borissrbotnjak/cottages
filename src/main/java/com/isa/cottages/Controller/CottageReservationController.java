package com.isa.cottages.Controller;

import com.isa.cottages.Model.CottageOwner;
import com.isa.cottages.Service.impl.CottageReservationServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/cottageReservations")
public class CottageReservationController {

    private CottageReservationServiceImpl reservationService;
    private UserServiceImpl userService;

    @Autowired
    public CottageReservationController(CottageReservationServiceImpl reservationService, UserServiceImpl userService) {
        this.reservationService = reservationService;
        this.userService = userService;
    }
//
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


}
