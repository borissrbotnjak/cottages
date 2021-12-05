package com.isa.cottages.Controller;

import com.isa.cottages.Model.FishingInstructorAdventure;
import com.isa.cottages.Model.Instructor;
import com.isa.cottages.Service.impl.FishingInstructorAdventureServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

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

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/allMyAdventures/{id}")
    public ModelAndView getAllMyAdventures(@PathVariable Long id, Model model, String keyword) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", instructor);
        if (instructor == null) {
            throw new Exception("Instructor does not exist.");
        }
        if (keyword != null) {
            model.addAttribute("adventures", this.adventureService.findByKeyword(keyword));
        } else {
            model.addAttribute("adventures", adventureService.findByInstructor(id));
        }
        return new ModelAndView("instructor/allMyAdventures");
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/addCottage/{id}")
    public ModelAndView addAdventureForm(@PathVariable Long id, Model model) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", instructor);
        FishingInstructorAdventure adventure = new FishingInstructorAdventure();
        model.addAttribute("adventure", adventure);

        Collection<FishingInstructorAdventure> adventures = this.adventureService.findByInstructor(id);
        model.addAttribute("adventures", adventures);

        return new ModelAndView("adventure/addAdventureForm");
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/allMyAdventures/{id}/editAdventure/{aid}")
    public ModelAndView updateAdventure(Model model, @PathVariable("id") Long id,
                                        @PathVariable("aid") Long aid) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", instructor);

        FishingInstructorAdventure adventure = this.adventureService.findById(aid);
        model.addAttribute("adventure", adventure);

        Collection<FishingInstructorAdventure> adventures = this.adventureService.findByInstructor(id);
        model.addAttribute("adventures", adventures);
        return new ModelAndView("adventure/editAdventure");
    }

}
