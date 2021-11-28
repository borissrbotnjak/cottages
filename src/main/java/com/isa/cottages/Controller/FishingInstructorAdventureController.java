package com.isa.cottages.Controller;

import com.isa.cottages.Model.FishingInstructorAdventure;
import com.isa.cottages.Service.impl.FishingInstructorAdventureServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/adventures")
public class FishingInstructorAdventureController {

    private FishingInstructorAdventureServiceImpl adventureService;
    private UserServiceImpl userService;

    @GetMapping("/allAdventures")
    public ModelAndView getAllAdventures(Model model, String keyword) {
        if (keyword != null) {
            model.addAttribute("instructors", this.adventureService.findByKeyword(keyword));
        } else {
            model.addAttribute("instructors", this.adventureService.findAll());
        }

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("instructor/instructors");
        } catch (Exception e) {
            System.out.println("\n\n\n error all adventures \n\n\n ");
            return new ModelAndView("home");
        }
    }

    @GetMapping("/{id}")
    public ModelAndView showAdventure(@PathVariable("id") Long id, Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("instructor", this.adventureService.findById(id));
        return new ModelAndView("instructor/instructor");
    }

    @GetMapping("/allAdventures/sortByInstructorNameDesc")
    public ModelAndView sortByInstructorNameDesc(Model model) {
        List<FishingInstructorAdventure> sorted = this.adventureService.sortByInstructorInfo(false);
        model.addAttribute("instructors", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("instructor/instructors");
        } catch (Exception e) {
            return new ModelAndView("home");
        }
    }

    @GetMapping("/allAdventures/sortByInstructorNameAsc")
    public ModelAndView sortByInstructorNameAsc(Model model) {
        List<FishingInstructorAdventure> sorted = this.adventureService.sortByInstructorInfo(true);
        model.addAttribute("instructors", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("instructor/instructors");
        } catch (Exception e) {
            return new ModelAndView("home");
        }
    }

    @GetMapping("/allAdventures/sortByAdventureNameDesc")
    public ModelAndView sortByAdventureNameDesc(Model model) {
        List<FishingInstructorAdventure> sorted = this.adventureService.findByOrderByAdventureNameDesc();
        model.addAttribute("instructors", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("instructor/instructors");
        } catch (Exception e) {
            return new ModelAndView("home");
        }
    }

    @GetMapping("/allAdventures/sortByAdventureNameAsc")
    public ModelAndView sortByAdventureNameAsc(Model model) {
        List<FishingInstructorAdventure> sorted = this.adventureService.findByOrderByAdventureNameAsc();
        model.addAttribute("instructors", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("instructor/instructors");
        } catch (Exception e) {
            return new ModelAndView("home");
        }
    }
    @GetMapping("/allAdventures/sortByRatingDesc")
    public ModelAndView sortByRatingDesc(Model model) {
        List<FishingInstructorAdventure> sorted = this.adventureService.findByOrderByRatingDesc();
        model.addAttribute("instructors", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("instructor/instructors");
        } catch (Exception e) {
            return new ModelAndView("home");
        }
    }

    @GetMapping("/allAdventures/sortByRatingAsc")
    public ModelAndView sortByRatingAsc(Model model) {
        List<FishingInstructorAdventure> sorted = this.adventureService.findByOrderByRatingAsc();
        model.addAttribute("instructors", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("instructor/instructors");
        } catch (Exception e) {
            return new ModelAndView("home");
        }
    }

    @GetMapping("/allAdventures/SortByAddressAsc")
    public ModelAndView orderByAddressAsc(Model model) {
        List<FishingInstructorAdventure> sorted = this.adventureService.findByOrderByAddressAsc();
        model.addAttribute("instructors", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("instructor/instructors");
        } catch (Exception e) {
            return new ModelAndView("home");
        }
    }

    @GetMapping("/allAdventures/SortByAddressDesc")
    public ModelAndView orderByAddressDesc(Model model) {
        List<FishingInstructorAdventure> sorted = this.adventureService.findByOrderByAddressDesc();
        model.addAttribute("instructors", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("instructor/instructors");
        } catch (Exception e) {
            return new ModelAndView("home");
        }
    }
}
