package com.isa.cottages.Controller;

import com.isa.cottages.Model.*;
import com.isa.cottages.Service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private ReviewServiceImpl reviewService;
    private UserServiceImpl userService;
    private BoatServiceImpl boatService;
    private CottageServiceImpl cottageService;
    private FishingInstructorAdventureServiceImpl adventureService;

    @Autowired
    ReviewController(ReviewServiceImpl reviewService, UserServiceImpl userService,
                     BoatServiceImpl boatService, CottageServiceImpl cottageService,
                     FishingInstructorAdventureServiceImpl adventureService) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.boatService = boatService;
        this.cottageService = cottageService;
        this.adventureService = adventureService;
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/boat/new/{entity_id}")
    public ModelAndView makeNewBoatReview(@PathVariable("entity_id") Long id, Model model) throws Exception{
        Client client = (Client) this.userService.getUserFromPrincipal();
        Review review = new Review();
        review.setBoat(this.boatService.findById(id));

        model.addAttribute("principal", client);
        model.addAttribute("review", review);
        model.addAttribute("entity_id", id);

        return new ModelAndView("review/new");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/boat/new/{entity_id}")
    public ModelAndView submitBoatReview(@PathVariable("entity_id") Long id, @ModelAttribute(name = "review") Review review,
                                            Model model) throws Exception {
        Client client = (Client) this.userService.getUserFromPrincipal();
        review.setClient(client);
        review.setBoat(this.boatService.findById(id));

        model.addAttribute("principal", client);
        this.reviewService.save(review);

        return new ModelAndView("redirect:/boatReservations/history");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/cottage/new/{entity_id}")
    public ModelAndView makeNewCottageReview(@PathVariable("entity_id") Long id, Model model) throws Exception{
        Client client = (Client) this.userService.getUserFromPrincipal();
        Review review = new Review();
        review.setCottage(this.cottageService.findById(id));

        model.addAttribute("principal", client);
        model.addAttribute("review", review);
        model.addAttribute("entity_id", id);

        return new ModelAndView("review/new");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/cottage/new/{entity_id}")
    public ModelAndView submitCottageReview(@PathVariable("entity_id") Long id, @ModelAttribute(name = "review") Review review,
                                            Model model) throws Exception {
        Client client = (Client) this.userService.getUserFromPrincipal();
        review.setClient(client);
        review.setCottage(this.cottageService.findById(id));

        model.addAttribute("principal", client);
        this.reviewService.save(review);

        return new ModelAndView("redirect:/cottageReservations/history");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/instructor/new/{entity_id}")
    public ModelAndView makeNewAdventureReview(@PathVariable("entity_id") Long id, Model model) throws Exception{
        Client client = (Client) this.userService.getUserFromPrincipal();
        Review review = new Review();
        review.setInstructor(this.adventureService.findById(id));

        model.addAttribute("principal", client);
        model.addAttribute("review", review);
        model.addAttribute("entity_id", id);

        return new ModelAndView("review/new");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/instructor/new/{entity_id}")
    public ModelAndView submitAdventureReview(@PathVariable("entity_id") Long id, @ModelAttribute(name = "review") Review review,
                                            Model model) throws Exception {
        Client client = (Client) this.userService.getUserFromPrincipal();
        review.setClient(client);
        review.setInstructor(this.adventureService.findById(id));

        model.addAttribute("principal", client);
        this.reviewService.save(review);

        return new ModelAndView("redirect:/instructorReservations/history");
    }

}
