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

@Controller
@AllArgsConstructor
@RequestMapping("/adventures")
public class FishingInstructorAdventureController {

    private FishingInstructorAdventureServiceImpl adventureService;
    private UserServiceImpl userService;

    @GetMapping("/allAdventures")
    public ModelAndView getAllAdventures(Model model, String keyword) throws Exception {
        if (keyword != null) {
            model.addAttribute("instructors", this.adventureService.findByKeyword(keyword));
        } else {
            model.addAttribute("instructors", this.adventureService.findAll());
        }

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("instructors");
        } catch (Exception e) {
            System.out.println("\n\n\n error all adventures \n\n\n ");
            return new ModelAndView("home");
        }
    }

    @GetMapping("/{id}")
    public ModelAndView showAdventure(@PathVariable("id") Long id, Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("instructor", this.adventureService.findById(id));
        return new ModelAndView("instructor");
    }
}
