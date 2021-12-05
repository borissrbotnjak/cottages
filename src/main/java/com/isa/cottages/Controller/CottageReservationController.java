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
@RequestMapping("/cottageReservations")
public class CottageReservationController {

    private CottageReservationServiceImpl reservationService;
    private UserServiceImpl userService;
    private CottageServiceImpl cottageService;
    private ReportServiceImpl reportService;

    @Autowired
    public CottageReservationController(CottageReservationServiceImpl reservationService,
                                        UserServiceImpl userService,
                                        CottageServiceImpl cottageService,
                                        ReportServiceImpl reportService) {
        this.reservationService = reservationService;
        this.userService = userService;
        this.cottageService = cottageService;
        this.reportService = reportService;
    }

    @GetMapping("/upcomingOwnersReservations/{id}")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView showUpcomingReservations(Model model, @PathVariable Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        model.addAttribute("cottageReservations", this.reservationService.getOwnersUpcomingReservations(id));

        return new ModelAndView("cottage/upcomingReservations");
    }

    @GetMapping("/pastOwnersReservations/{id}")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView showReservationHistory(Model model, String keyword, @PathVariable("id") Long id, String email) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);
//        Client client = this.clientService.findByEmail(email);
//        model.addAttribute("client", client);

        if (keyword != null) {
            model.addAttribute("cottageReservations", this.reservationService.findClient(keyword));
        } else {
            model.addAttribute("cottageReservations", this.reservationService.getOwnersPastReservations(id));
        }
        return new ModelAndView("cottage/pastReservations");
    }

    @GetMapping("/allDiscounts/{id}")
    public ModelAndView getDiscountsByCottage(@PathVariable Long id, Model model) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        Cottage cottage = this.cottageService.findById(id);
        model.addAttribute("cottage", cottage);

        if(cottage == null) {
            throw new Exception("Cottage with this id does not exist.");
        }
        model.addAttribute("cottageReservations", reservationService.findDiscountsByCottage(id));

        return new ModelAndView("cottage/allDiscounts");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/{id}/defineDiscount")
    public ModelAndView defineDiscount(@PathVariable Long id, Model model) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        model.addAttribute("cottage", this.cottageService.findById(id));
        CottageReservation cottageReservation = new CottageReservation();

        model.addAttribute("cottageReservation", cottageReservation);

        Collection<CottageReservation> cottageReservations = this.reservationService.findDiscountsByCottage(id);
        model.addAttribute("cottageReservations", cottageReservations);

        return new ModelAndView("cottage/defineDiscount");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @PostMapping("/{id}/defineDiscount/submit")
    public ModelAndView defineDiscount(@PathVariable Long id, @ModelAttribute CottageReservation cottageReservation, Model model) throws Exception {
//        if (this.cottageService.findById(cottage.getId()) != null) {
//            throw new ResourceConflictException(cottage.getId(), "Cottage with this id already exist.");
//        }
        Collection<CottageReservation> cottageReservations = this.reservationService.findDiscountsByCottage(id);
        model.addAttribute("cottageReservations", cottageReservations);

        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        cottageReservation.setCottageOwner((CottageOwner) this.userService.getUserFromPrincipal());
        cottageReservation.setCottage(this.cottageService.findById(id));
        this.reservationService.saveDiscount(cottageReservation);
        return new ModelAndView("redirect:/cottageReservations/allDiscounts/{id}/");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/writeReport/{id}")
    public ModelAndView reportForm(Model model, @PathVariable Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);
        model.addAttribute("cottageReservations", this.reservationService.getOwnersPastReservations(id));

        Report report = new Report();
        model.addAttribute("report", report);

        return new ModelAndView("cottage/report");
    }


    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @PostMapping("/writeReport/{id}/submit")
    public ModelAndView reportFormSubmit(Model model, @ModelAttribute Report report,
                                         @PathVariable Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);
        model.addAttribute("cottageReservations", this.reservationService.getOwnersPastReservations(id));

        report.setCottageOwner(cottageOwner);
        reportService.save(report);
        return new ModelAndView("redirect:/cottageReservations/pastOwnersReservations/{id}/");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/viewCalendar/{id}")
    public ModelAndView viewCalendar (Model model, @PathVariable Long id, String keyword) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);
        if (keyword != null) {
            model.addAttribute("cottageReservations", this.reservationService.findClient(keyword));
        } else {
            model.addAttribute("cottageReservations", this.reservationService.getAllOwnersReservations(id));
        }
        return new ModelAndView("cottage/calendar");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/makeCottageReservationWithClient")
    public ModelAndView makeReservationWithClient(Model model) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        return new ModelAndView("cottage/makeCottageReservationWithClient");
    }

        @GetMapping("/history")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView showReservationHistory(Model model, String keyword) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        if (keyword != null) {
            // TODO: Dodaj pretragu
            //model.addAttribute("reservations", this.reservationService.findByKeyword(keyword));
        } else {
            model.addAttribute("cottageReservations", this.reservationService.getPastReservations());
        }
        // TODO: dodaj stranicu
        return new ModelAndView("cottage/pastReservations");
    }

    @GetMapping("/upcoming")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView showUpcomingReservations(Model model, String keyword) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        if (keyword != null) {
            // TODO: Dodaj pretragu
            //model.addAttribute("reservations", this.reservationService.findByKeyword(keyword));
        } else {
            model.addAttribute("cottageReservations", this.reservationService.getUpcomingReservations());
        }
        // TODO: Dopuni stranicu
        return new ModelAndView("cottage/upcomingReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByDateAsc")
    public ModelAndView sortPastReservationsByDateAsc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("cottageReservations", this.reservationService.findByOrderByStartTimeAsc());

        return new ModelAndView("cottage/pastReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByDateDesc")
    public ModelAndView sortPastReservationsByDateDesc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("cottageReservations", this.reservationService.findByOrderByStartTimeDesc());

        return new ModelAndView("cottage/pastReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByDurationAsc")
    public ModelAndView sortPastReservationsByDurationAsc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("cottageReservations", this.reservationService.findByOrderByDurationAsc());

        return new ModelAndView("cottage/pastReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByDurationDesc")
    public ModelAndView sortPastReservationsByDurationDesc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("cottageReservations", this.reservationService.findByOrderByDurationDesc());

        return new ModelAndView("cottage/pastReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByPriceAsc")
    public ModelAndView sortPastReservationsByPriceAsc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("cottageReservations", this.reservationService.findByOrderByPriceAsc());

        return new ModelAndView("cottage/pastReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByPriceDesc")
    public ModelAndView sortPastReservationsByPriceDesc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("cottageReservations", this.reservationService.findByOrderByPriceDesc());

        return new ModelAndView("cottage/pastReservations");
    }
}
