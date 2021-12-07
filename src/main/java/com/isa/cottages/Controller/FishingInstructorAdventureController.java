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
import org.springframework.web.bind.annotation.PostMapping;
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
    @GetMapping("/addAdventure/{id}")
    public ModelAndView addAdventureForm(@PathVariable Long id, Model model) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", instructor);
        FishingInstructorAdventure adventure = new FishingInstructorAdventure();
        model.addAttribute("adventure", adventure);

        Collection<FishingInstructorAdventure> adventures = this.adventureService.findByInstructor(id);
        model.addAttribute("adventures", adventures);

        return new ModelAndView("instructor/addAdventureForm");
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
        return new ModelAndView("instructor/editAdventure");
    }

   /* @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping(value = "/allMyAdventures/{id}/remove/{aid}")
    public ModelAndView removeCottage(@PathVariable Long id,
                                      @PathVariable Long aid,
                                      Model model) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", instructor);

        FishingInstructorAdventure instructor = this.adventureService.findById(id);

        boolean delete = this.adventureService.canUpdateOrDelete(id);
        if (!delete) {
            return new ModelAndView("cottage/errors/errorDeleteCottage");
        } else {
            this.cottageService.removeCottage(cottage, id);
            cottage.setDeleted(true);
            this.cottageService.updateCottage(cottage);
        }
        Collection<Cottage> cottages = this.cottageService.findByCottageOwner(cid);
        model.addAttribute("cottages", cottages);

        return new ModelAndView("redirect:/cottages/allMyCottages/{id}" );
    }*/

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/{id}/edit/submit")
    public ModelAndView edit(@PathVariable Long id, Model model, FishingInstructorAdventure adventure) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", instructor);

        Collection<FishingInstructorAdventure> adventures = this.adventureService.findByInstructor(id);
        model.addAttribute("adventures", adventures);

        adventure.setInstructor((Instructor) this.userService.getUserFromPrincipal());

        boolean update = this.adventureService.canUpdateOrDelete(id);
        if (!update) {
            return new ModelAndView("adventure/errors/errorUpdateAdventure");
        } else {
            this.adventureService.updateAdventure(adventure);
        }

        return new ModelAndView("redirect:/adventures/{id}/");
    }

}
