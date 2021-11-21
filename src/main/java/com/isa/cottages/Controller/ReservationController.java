package com.isa.cottages.Controller;

import com.isa.cottages.Model.*;
import com.isa.cottages.Service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private ReservationServiceImpl reservationService;
    private UserServiceImpl userService;
    private CottageServiceImpl cottageService;
    private ReportServiceImpl reportService;
    private ClientServiceImpl clientService;

    @Autowired
    public ReservationController(ReservationServiceImpl reservationService,
                                 UserServiceImpl userService,
                                 CottageServiceImpl cottageService,
                                 ReportServiceImpl reportService,
                                 ClientServiceImpl clientService) {
        this.reservationService = reservationService;
        this.userService = userService;
        this.cottageService = cottageService;
        this.reportService = reportService;
        this.clientService = clientService;
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
        model.addAttribute("cottageReservations", reservationService.findDiscountsByCottage(id));

        return new ModelAndView("allActionsByCottage");
    }

    @GetMapping("/upcomingReservations/cottages")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView showUpcomingReservations(Model model) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        model.addAttribute("cottageReservations", this.reservationService.getUpcomingReservations());

        return new ModelAndView("upcomingCottageReservations");
    }

    @GetMapping("/reservationHistory/cottages")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView showReservationHistory(Model model, String keyword) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        if (keyword != null) {
            model.addAttribute("cottageReservations", this.reservationService.findClient(keyword));
        } else {
            model.addAttribute("cottageReservations", this.reservationService.getPastReservations());
        }
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

        Collection<CottageReservation> cottageReservations = this.reservationService.findDiscountsByCottage(id);
        model.addAttribute("cottageReservations", cottageReservations);

        return new ModelAndView("defineAction");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @PostMapping("/{id}/defineAction/submit")
    public ModelAndView defineAction(@PathVariable Long id, @ModelAttribute CottageReservation cottageReservation, Model model) throws Exception {
//        if (this.cottageService.findById(cottage.getId()) != null) {
//            throw new ResourceConflictException(cottage.getId(), "Cottage with this id already exist.");
//        }
        Collection<CottageReservation> cottageReservations = this.reservationService.findDiscountsByCottage(id);
        model.addAttribute("cottageReservations", cottageReservations);

        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        cottageReservation.setCottageOwner((CottageOwner) this.userService.getUserFromPrincipal());
        cottageReservation.setCottage((Cottage) this.cottageService.findById(id));
        this.reservationService.saveDiscount(cottageReservation);
        return new ModelAndView("redirect:/cottages/{id}/");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/writeReport")
    public ModelAndView reportForm(Model model) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        Report report = new Report();
        model.addAttribute("report", report);

        return new ModelAndView("report");
    }


    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @PostMapping("/writeReport/submit")
    public ModelAndView reportFormSubmit(Model model, @ModelAttribute Report report) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        report.setCottageOwner(cottageOwner);
        this.reportService.save(report);
        return new ModelAndView("redirect:/cottageReservations/reservationHistory/");
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
