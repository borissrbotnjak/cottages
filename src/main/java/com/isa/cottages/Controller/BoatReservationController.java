package com.isa.cottages.Controller;

import com.isa.cottages.Model.*;
import com.isa.cottages.Service.impl.BoatReservationServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
import com.isa.cottages.Service.impl.BoatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
@RequestMapping("/boatReservations")
public class BoatReservationController {

    private UserServiceImpl userService;
    private BoatReservationServiceImpl reservationService;
    private BoatServiceImpl boatService;

    @Autowired
    public BoatReservationController(UserServiceImpl userService, BoatReservationServiceImpl reservationService,
                                     BoatServiceImpl boatService) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.boatService = boatService;
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView showReservationHistory(Model model, String keyword) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        if (keyword != null) {
            //model.addAttribute("reservations", );
        } else {
            model.addAttribute("reservations", this.reservationService.getPastReservations());
        }
        return new ModelAndView("boat/reservationHistory");
    }

    @GetMapping("/upcoming")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView showUpcomingReservations(Model model, String keyword) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        if (keyword != null) {
            //model.addAttribute("reservations", );
        } else {
            model.addAttribute("reservations", this.reservationService.getUpcomingReservations());
        }
        return new ModelAndView("boat/upcomingReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByDateAsc")
    public ModelAndView sortPastReservationsByDateAsc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("reservations", this.reservationService.findByOrderByStartTimeAsc());

        return new ModelAndView("boat/reservationHistory");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByDateDesc")
    public ModelAndView sortPastReservationsByDateDesc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("reservations", this.reservationService.findByOrderByStartTimeDesc());

        return new ModelAndView("boat/reservationHistory");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByDurationAsc")
    public ModelAndView sortPastReservationsByDurationAsc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("reservations", this.reservationService.findByOrderByDurationAsc());

        return new ModelAndView("boat/reservationHistory");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByDurationDesc")
    public ModelAndView sortPastReservationsByDurationDesc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("reservations", this.reservationService.findByOrderByDurationDesc());

        return new ModelAndView("boat/reservationHistory");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByPriceAsc")
    public ModelAndView sortPastReservationsByPriceAsc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("reservations", this.reservationService.findByOrderByPriceAsc());

        return new ModelAndView("boat/reservationHistory");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByPriceDesc")
    public ModelAndView sortPastReservationsByPriceDesc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("reservations", this.reservationService.findByOrderByPriceDesc());

        return new ModelAndView("boat/reservationHistory");
    }

    @GetMapping("/allDiscounts/{id}")
    public ModelAndView getDiscountsByBoat(@PathVariable Long id, Model model) throws Exception {
        User user = this.userService.getUserFromPrincipal();
        model.addAttribute("principal", user);

        Boat boat = this.boatService.findById(id);
        model.addAttribute("boat", boat);

        if(boat == null) {
            throw new Exception("Boat with this id does not exist.");
        }
        model.addAttribute("boatReservations", reservationService.findDiscountsByBoat(id));

        return new ModelAndView("boat/allDiscounts");
    }

    @PreAuthorize("hasRole('BOAT_OWNER')")
    @GetMapping("/{id}/defineDiscount")
    public ModelAndView defineDiscount(@PathVariable Long id, Model model) throws Exception {
        User user = this.userService.getUserFromPrincipal();
        model.addAttribute("principal", user);

        model.addAttribute("boat", this.boatService.findById(id));
        BoatReservation boatReservation = new BoatReservation();

        model.addAttribute("boatReservation", boatReservation);

        Collection<BoatReservation> boatReservations = this.reservationService.findDiscountsByBoat(id);
        model.addAttribute("boatReservations", boatReservations);

        return new ModelAndView("boat/defineDiscount");
    }

    @PreAuthorize("hasRole('BOAT_OWNER')")
    @PostMapping("/{id}/defineDiscount/submit")
    public ModelAndView defineDiscount(@PathVariable Long id, @ModelAttribute BoatReservation boatReservation, Model model) throws Exception {
//        if (this.boatService.findById(boat.getId()) != null) {
//            throw new ResourceConflictException(boat.getId(), "Boat with this id already exist.");
//        }
        Collection<BoatReservation> boatReservations = this.reservationService.findDiscountsByBoat(id);
        model.addAttribute("boatReservations", boatReservations);

        User user = this.userService.getUserFromPrincipal();
        model.addAttribute("principal", user);

        boatReservation.setBoatOwner((BoatOwner) this.userService.getUserFromPrincipal());
        boatReservation.setBoat(this.boatService.findById(id));
        boatReservation.setDiscount(true);
        this.reservationService.saveDiscount(boatReservation);
        return new ModelAndView("redirect:/boatReservations/allDiscounts/{id}/");
    }
}
