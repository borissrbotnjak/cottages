package com.isa.cottages.Controller;

import com.isa.cottages.Model.*;
import com.isa.cottages.Service.impl.BoatReservationServiceImpl;
import com.isa.cottages.Service.impl.ReportServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
import com.isa.cottages.Service.impl.BoatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/boatReservations")
public class BoatReservationController {

    private UserServiceImpl userService;
    private BoatReservationServiceImpl reservationService;
    private BoatServiceImpl boatService;
    private ReportServiceImpl reportService;

    @Autowired
    public BoatReservationController(UserServiceImpl userService, BoatReservationServiceImpl reservationService,
                                     BoatServiceImpl boatService, ReportServiceImpl reportService) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.boatService = boatService;
        this.reservationService = reservationService;
        this.reportService = reportService;
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

    @GetMapping("/upcomingOwnersReservations/{id}")
    @PreAuthorize("hasRole('BOAT_OWNER')")
    public ModelAndView showUpcomingReservations(Model model, @PathVariable Long id) throws Exception {
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", boatOwner);

        model.addAttribute("boatReservations", this.reservationService.getOwnersUpcomingReservations(id));

        return new ModelAndView("boat/upcomingReservations");
    }

    @GetMapping("/pastOwnersReservations/{id}")
    @PreAuthorize("hasRole('BOAT_OWNER')")
    public ModelAndView showReservationHistory(Model model, String keyword,
                                               @PathVariable("id") Long id) throws Exception {
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", boatOwner);
//        Client client = this.clientService.findByEmail(email);
//        model.addAttribute("client", client);

        if (keyword != null) {
            model.addAttribute("boatReservations", this.reservationService.findClient(keyword));
        } else {
            model.addAttribute("boatReservations", this.reservationService.getOwnersPastReservations(id));
        }
        return new ModelAndView("boat/reservationHistory");
    }

    @PreAuthorize("hasRole('BOAT_OWNER')")
    @GetMapping("/writeReport/{oid}/{id}/{aid}")
    public ModelAndView reportForm(Model model, @PathVariable Long id,
                                   @PathVariable Long aid, @PathVariable Long oid) throws Exception {
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", boatOwner);
        model.addAttribute("boatReservations", this.reservationService.getOwnersPastReservations(oid));
        Report report = new Report();
        model.addAttribute("report", report);
        report.setClient((Client) this.userService.findById(id));
        report.setAdmin((SystemAdministrator) this.userService.findById(aid));
        model.addAttribute("id", id);
        model.addAttribute("aid", aid);
        model.addAttribute("oid", oid);

        return new ModelAndView("boat/report");
    }


    @PreAuthorize("hasRole('BOAT_OWNER')")
    @PostMapping("/writeReport/{oid}/{id}/{aid}/submit")
    public ModelAndView reportFormSubmit(Model model, @ModelAttribute Report report,
                                         @PathVariable Long id, @PathVariable Long aid,
                                         @PathVariable Long oid,
                                         Client client) throws Exception {
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", boatOwner);
        model.addAttribute("boatReservations", this.reservationService.getOwnersPastReservations(oid));

        model.addAttribute("report", report);
        List<Report> reports = reportService.findBoatOwnersReports(boatOwner.getId());
        model.addAttribute("reports", reports);
        SystemAdministrator admin = (SystemAdministrator) this.userService.findById(aid);
        model.addAttribute("id", id);
        model.addAttribute("aid", aid);
        model.addAttribute("oid", oid);

        report.setBoatOwner(boatOwner);
        report.setClient((Client) this.userService.findById(id));
        report.setAdmin(admin);
        int penalties = 0;
        report.getClient().setPenalties(penalties);

        if(report.getPenal() == report.getPenal().TRUE) {
            admin.getReports().add(report);
            report.setApproved(true);
            penalties += 1;
            report.getClient().setPenalties(penalties);
        }
        if (report.getDidAppear() == report.getDidAppear().FALSE) {
            client.getPenalties();
            penalties += 1;
            report.getClient().setPenalties(penalties);
        }

        reportService.save(report);
        return new ModelAndView("redirect:/boatReservations/pastOwnersReservations/{oid}");
    }

    @PreAuthorize("hasRole('BOAT_OWNER')")
    @GetMapping("/{id}/makeReservationWithClient/{bid}/{cid}")
    public ModelAndView makeReservation(@PathVariable Long id, @PathVariable Long bid, @PathVariable Long cid,
                                        Model model, Authentication auth) throws Exception {
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", boatOwner);

        Client client = (Client) this.userService.findById(cid);
        model.addAttribute("client", client);
        model.addAttribute("boat", this.boatService.findById(bid));
        model.addAttribute("client", this.userService.findByEmail(auth.getName()));

        Reservation reservation = new Reservation();
        model.addAttribute("reservation", reservation);

        Collection<BoatReservation> boatReservations = this.reservationService.findDiscountsByBoat(bid);
        model.addAttribute("boatReservations", boatReservations);

        return new ModelAndView("boat/makeReservationWithClient");
    }

    @PreAuthorize("hasRole('BOAT_OWNER')")
    @PostMapping("/{id}/makeReservationWithClient/submit")
    public ModelAndView makeReservation(@PathVariable Long id, @ModelAttribute BoatReservation boatReservation,
                                        Model model, Client client) throws Exception {
//        if (this.boatService.findById(boat.getId()) != null) {
//            throw new ResourceConflictException(boat.getId(), "Boat with this id already exist.");
//        }
        Collection<BoatReservation> boatReservations = this.reservationService.findDiscountsByBoat(id);
        model.addAttribute("boatReservations", boatReservations);

        User user = this.userService.getUserFromPrincipal();
        model.addAttribute("principal", user);

        model.addAttribute("freeReservations", this.reservationService.getOwnersFreeReservations(id));

        boatReservation.setBoatOwner((BoatOwner) this.userService.getUserFromPrincipal());
        boatReservation.setBoat(this.boatService.findById(id));
        boatReservation.setDiscount(false);
        boatReservation.setClient(client);
        this.reservationService.saveReservation(boatReservation);
        return new ModelAndView("redirect:/boatReservations/upcomingOwnersReservations/{oid}/");
    }

    @PreAuthorize("hasRole('BOAT_OWNER')")
    @GetMapping("/viewCalendar/{id}")
    public ModelAndView viewCalendar (Model model, @PathVariable Long id, String keyword) throws Exception {
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", boatOwner);
        if (keyword != null) {
            model.addAttribute("boatReservations", this.reservationService.findClient(keyword));
        } else {
            model.addAttribute("boatReservations", this.reservationService.getAllOwnersReservations(id));
        }
        return new ModelAndView("boat/calendar");
    }
}
