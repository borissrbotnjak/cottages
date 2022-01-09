package com.isa.cottages.Controller;

import com.isa.cottages.Model.*;
import com.isa.cottages.Service.impl.AdditionalServiceServiceImpl;
import com.isa.cottages.Service.impl.BoatReservationServiceImpl;
import com.isa.cottages.Service.impl.BoatServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/boatReservations")
public class BoatReservationController {

    private UserServiceImpl userService;
    private BoatReservationServiceImpl reservationService;
    private BoatServiceImpl boatService;
    private AdditionalServiceServiceImpl serviceService;

    @Autowired
    public BoatReservationController(UserServiceImpl userService, BoatReservationServiceImpl reservationService, BoatServiceImpl boatService,
                                                                                                                AdditionalServiceServiceImpl serviceService) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.boatService = boatService;
        this.serviceService = serviceService;
    }
/*
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView makeOne(Model model, LocalDate startTime, LocalDate endTime) throws Exception {
        Client client = (Client) this.userService.getUserFromPrincipal();
        if ((startTime != null) || (endTime != null)) {
            // model.addAttribute("reservations", this.reservationService.findAllAvailable(startTime, endTime));
            model.addAttribute("reservations", this.reservationService.findAvailable_2(startTime, endTime));
            model.addAttribute("startTime", startTime);
            model.addAttribute("endTime", endTime);
        } else {
            model.addAttribute("reservations", new ArrayList<BoatReservation>());
        } // TODO:
        model.addAttribute("principal", client);
        return new ModelAndView("boat/available");
    }*/
    @GetMapping("/chooseTime")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView chooseDate(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        model.addAttribute("startDate", LocalDate.now());
        model.addAttribute("endDate", LocalDate.now());
        model.addAttribute("numPersons", 1);

        return new ModelAndView("reservationTime");
    }

    @PostMapping("/chooseTime")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView chooseTime(Model model, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
                                                                                    @RequestParam("numPersons") Integer numPersons) throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate sd = LocalDate.parse(startDate, formatter);
        LocalDate ed = LocalDate.parse(endDate, formatter);

        model.addAttribute("startDate", sd);
        model.addAttribute("endDate", ed);
        model.addAttribute("numPersons", numPersons);

        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        return new ModelAndView("redirect:/boatReservations/available");
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView showAvailable(Model model, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
                                                                                        @RequestParam("numPersons") Integer numPersons) throws Exception {
        // TODO:
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate sd = LocalDate.parse(startDate, formatter);
        LocalDate ed = LocalDate.parse(endDate, formatter);

        model.addAttribute("startDate", sd);
        model.addAttribute("endDate", ed);
        model.addAttribute("numPersons", numPersons);

        model.addAttribute("boats", this.boatService.findAllAvailable(sd, ed, numPersons));
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        return new ModelAndView("boat/available");
    }

    @GetMapping("/select/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView selectEntity(@PathVariable Long id, Model model, @RequestParam("startDate") String startDate,
                                     @RequestParam("endDate") String endDate, @RequestParam("numPersons") Integer numPersons) throws Exception {

        Client client = (Client) this.userService.getUserFromPrincipal();

        model.addAttribute("principal", client);
        model.addAttribute("services", this.boatService.findById(id).getAdditionalServices());
        model.addAttribute("boat_id", id);
        model.addAttribute("startDateString", startDate);
        model.addAttribute("endDateString", endDate);
        model.addAttribute("numPersons", numPersons);

        BoatReservation reservation = new BoatReservation();
        model.addAttribute("reservation", reservation);

        return new ModelAndView("boat/additionalServices");
    }

    @PostMapping("/done/{boatId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView makeReservation(@PathVariable("boatId") Long boatId, Model model, @ModelAttribute("reservation") BoatReservation reservation) throws Exception {

        Boat boat = this.boatService.findById(boatId);
        BoatReservation res = this.reservationService.makeReservation(reservation, boat);

        model.addAttribute("reservation", res);
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        return new ModelAndView("redirect:/boatReservations/success");
    }

    @GetMapping("/success")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView reservationConfirmation(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        return new ModelAndView("reservationSuccess");
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

}
