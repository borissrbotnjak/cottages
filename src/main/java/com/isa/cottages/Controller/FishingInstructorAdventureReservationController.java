package com.isa.cottages.Controller;

import com.isa.cottages.Email.EmailSender;
import com.isa.cottages.Model.Instructor;
import com.isa.cottages.Service.impl.FishingInstructorAdventureReservationServiceImpl;
import com.isa.cottages.Service.impl.FishingInstructorAdventureServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/adventureReservations")
public class FishingInstructorAdventureReservationController {
    private final FishingInstructorAdventureReservationServiceImpl reservationService;
    private final EmailSender emailSender;
    private final UserServiceImpl userService;
    private final FishingInstructorAdventureServiceImpl adventureService;

    @Autowired
    public FishingInstructorAdventureReservationController(EmailSender emailSender, FishingInstructorAdventureServiceImpl adventureService,
                                                           UserServiceImpl userService, FishingInstructorAdventureReservationServiceImpl reservationService) {
        this.emailSender = emailSender;
        this.adventureService = adventureService;
        this.userService = userService;
        this.reservationService = reservationService;
    }

    //<editor-fold desc="Get upcoming reservations">
    @GetMapping("/upcomingInstructorsReservations/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ModelAndView upcomingReservations(Model model, @PathVariable Long id) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", instructor);

        model.addAttribute("adventureReservations", this.reservationService.getInstructorsUpcomingReservations(id));

        return new ModelAndView("instructor/upcomingReservations");
    }
    //</editor-fold>
}
