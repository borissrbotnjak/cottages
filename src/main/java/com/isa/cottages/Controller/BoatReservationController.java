package com.isa.cottages.Controller;

import com.isa.cottages.Service.impl.BoatReservationServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/boatReservations")
public class BoatReservationController {

    private UserServiceImpl userService;
    private BoatReservationServiceImpl reservationService;

    @Autowired
    public BoatReservationController(UserServiceImpl userService, BoatReservationServiceImpl reservationService) {
        this.userService = userService;
        this.reservationService = reservationService;
    }

    @GetMapping("/reservationHistory/boats")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView showBoatsHistory(Model model, String keyword) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        if (keyword != null) {
            //model.addAttribute("reservations", this.reservationService.findClient(keyword));
        } else {
            model.addAttribute("reservations", this.reservationService.getPastBoatReservations());
        }
        return new ModelAndView("boatsReservationHistory");
    }
}
