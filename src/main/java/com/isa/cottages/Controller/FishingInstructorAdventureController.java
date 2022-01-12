package com.isa.cottages.Controller;

import com.isa.cottages.Model.AdditionalService;
import com.isa.cottages.Model.FishingInstructorAdventure;
import com.isa.cottages.Model.Instructor;
import com.isa.cottages.Service.impl.FishingInstructorAdventureServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
        model.addAttribute("adventure", this.adventureService.findById(id));
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

    //<editor-fold desc="Add adventure get/post">
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
    @PostMapping("/addAdventure/{id}/submit")
    public ModelAndView addAdventure(@PathVariable Long id, @ModelAttribute FishingInstructorAdventure adventure,
                                     @RequestParam("image") MultipartFile image,
                                     Model model) throws Exception {

        Path path = Paths.get("C:\\Users\\User\\IdeaProjects\\cottages\\uploads");
        try {
            InputStream inputStream = image.getInputStream();
            Files.copy(inputStream, path.resolve(Objects.requireNonNull(image.getOriginalFilename())),
                    StandardCopyOption.REPLACE_EXISTING);
            adventure.setImageUrl(image.getOriginalFilename().toLowerCase());
            model.addAttribute("adventure", adventure);
            model.addAttribute("imageUrl", adventure.getImageUrl());

        } catch (Exception e) {
            e.printStackTrace();
        }

        Collection<FishingInstructorAdventure> adventures = this.adventureService.findByInstructor(id);
        model.addAttribute("adventures", adventures);

        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", instructor);

        adventure.setInstructor((Instructor) this.userService.getUserFromPrincipal());

        this.adventureService.saveAdventure(adventure);
        return new ModelAndView("redirect:/adventures/allMyAdventures/{id}/");
    }
    //</editor-fold>

    //<editor-fold desc="Remove adventure get">
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping(value = "/allMyAdventures/{id}/remove/{aid}")
    public ModelAndView removeAdventure(@PathVariable Long aid,
                                        @PathVariable Long id,
                                        Model model) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", instructor);

        FishingInstructorAdventure adventure = this.adventureService.findById(aid);

        if (!this.adventureService.canUpdateOrDelete(aid)) {
            return new ModelAndView("instructor/errors/errorDeleteAdventure");
        } else {
            this.adventureService.removeAdventure(adventure);
            adventure.setDeleted(true);
            this.adventureService.updateAdventure(adventure);
        }
        Collection<FishingInstructorAdventure> adventures = this.adventureService.findByInstructor(aid);
        model.addAttribute("adventures", adventures);

        return new ModelAndView("redirect:/adventures/allMyAdventures/{id}");
    }
    //</editor-fold>

    //<editor-fold desc="Update adventure post/get">
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("{id}/edit/submit")
    public ModelAndView updateAdventure(@PathVariable Long id, Model model, FishingInstructorAdventure adventure,
                                        @RequestParam("image") MultipartFile image) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", instructor);

        Collection<FishingInstructorAdventure> adventures = this.adventureService.findByInstructor(id);
        model.addAttribute("adventures", adventures);

        adventure.setInstructor((Instructor) this.userService.getUserFromPrincipal());

        Path path = Paths.get("C:\\Users\\User\\IdeaProjects\\cottages\\uploads");
        try {
            InputStream inputStream = image.getInputStream();
            Files.copy(inputStream, path.resolve(image.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            adventure.setImageUrl(image.getOriginalFilename().toLowerCase());
            model.addAttribute("adventure", adventure);
            model.addAttribute("imageUrl", adventure.getImageUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean update = this.adventureService.canUpdateOrDelete(id);
        if (!update) {
            return new ModelAndView("instructor/errors/errorDeleteAdventure");
        } else {
            this.adventureService.updateAdventure(adventure);
        }
        return new ModelAndView("redirect:/adventures/{id}/");
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

    //</editor-fold>

    //<editor-fold desc="Define availability get/post">
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/{id}/defineAvailability")
    public ModelAndView defineAvailability(Model model, @PathVariable Long id) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", instructor);

        FishingInstructorAdventure adventure = this.adventureService.findById(id);
        model.addAttribute("adventure", adventure);

        return new ModelAndView("instructor/defineAvailability");
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/{id}/defineAvailability/submit")
    public ModelAndView defineAvailability(Model model, @PathVariable Long id, @ModelAttribute FishingInstructorAdventure adventure) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", instructor);

        Collection<FishingInstructorAdventure> adventures = this.adventureService.findByInstructor(id);
        model.addAttribute("adventures", adventures);
        model.addAttribute("adventure", adventure);

        adventure.setInstructor((Instructor) this.userService.getUserFromPrincipal());
        this.adventureService.defineAvailability(adventure);

        return new ModelAndView("redirect:/adventures/{id}/");
    }
    //</editor-fold>



    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/allMyAdventures/{id}/additionalServices/{aid}")
    public ModelAndView additionalServices(Model model, @PathVariable("id") Long id,
                                           @PathVariable("aid") Long aid) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", instructor);

        FishingInstructorAdventure adventure = this.adventureService.findById(aid);
        model.addAttribute("adventure", adventure);

        Collection<AdditionalService> services = this.adventureService.findServicesByAdventure(adventure);
        model.addAttribute("services", services);

        AdditionalService newService = new AdditionalService();
        model.addAttribute("newService", newService);

        return new ModelAndView("instructor/additionalServices");
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/addAdventure/{id}/submit/{adventureId}")
    public ModelAndView addService(@PathVariable Long id, @PathVariable Long adventureId, @ModelAttribute AdditionalService service,
                                   Model model) throws Exception {

        service.setAdventure(this.adventureService.findById(adventureId));
        this.adventureService.saveService(service);


        Collection<FishingInstructorAdventure> adventures = this.adventureService.findByInstructor(id);
        model.addAttribute("adventures", adventures);

        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", instructor);
        return new ModelAndView("redirect:/adventures/allMyAdventures/{id}/");
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
