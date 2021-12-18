package com.isa.cottages.Controller;

import com.isa.cottages.Model.AdditionalService;
import com.isa.cottages.Model.Cottage;
import com.isa.cottages.Model.CottageOwner;
import com.isa.cottages.Service.impl.CottageServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

@Controller
@RequestMapping(value = "/cottages")
public class CottageController {

    private CottageServiceImpl cottageService;
    private UserServiceImpl userService;

    @Autowired
    public CottageController(CottageServiceImpl cottageService, UserServiceImpl userService) {
        this.cottageService = cottageService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/addCottage/{id}")
    public ModelAndView addCottageForm(@PathVariable Long id, Model model) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);
        Cottage cottage = new Cottage();
        model.addAttribute("cottage", cottage);

        for(int i=1; i<=3; i++) {
            cottage.addAdditionalService(new AdditionalService());
        }
        List<AdditionalService> additionalService = new ArrayList<>();
        model.addAttribute("additionalService", additionalService);

        Collection<Cottage> cottages = this.cottageService.findByCottageOwner(id);
        model.addAttribute("cottages", cottages);

        return new ModelAndView("cottage/addCottageForm");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @PostMapping("/addCottage/{id}/submit")
    public ModelAndView addCottage(@PathVariable Long id, @ModelAttribute Cottage cottage,
                                   @ModelAttribute AdditionalService additionalService,
                                   @RequestParam("image") MultipartFile image,
                                   Model model) throws Exception {
//        if (this.cottageService.findById(cottage.getId()) != null) {
//            throw new ResourceConflictException(cottage.getId(), "Cottage with this id already exist.");
//        }
        Path path = Paths.get("C:\\Users\\Dijana\\Desktop\\Cottages\\cottages\\uploads");
        try {
            InputStream inputStream = image.getInputStream();
            Files.copy(inputStream, path.resolve(image.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            cottage.setImageUrl(image.getOriginalFilename().toLowerCase());
            model.addAttribute("cottage", cottage);
            model.addAttribute("imageUrl", cottage.getImageUrl());

        } catch (Exception e) {
            e.printStackTrace();
        }

        Collection<Cottage> cottages = this.cottageService.findByCottageOwner(id);
        model.addAttribute("cottages", cottages);

        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        cottage.setCottageOwner((CottageOwner) this.userService.getUserFromPrincipal());
        cottage.setAdditionalServices(cottage.getAdditionalServices());
        additionalService.setCottage(additionalService.getCottage());

        model.addAttribute("additionalService", additionalService);
        model.addAttribute("additionalServices", cottage.getAdditionalServices());

        this.cottageService.saveCottage(cottage);
        this.cottageService.saveAdditionalService(additionalService);
        return new ModelAndView("redirect:/cottages/allMyCottages/{id}/");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/allMyCottages/{id}")
    public ModelAndView getAllMyCottages (@PathVariable Long id, Model model, String keyword) throws Exception{
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);
        if(cottageOwner == null) {
            throw new Exception("Cottage owner does not exist.");
        }
        if (keyword != null) {
            model.addAttribute("cottages", this.cottageService.findByKeyword(keyword));
        } else {
            model.addAttribute("cottages", cottageService.findByCottageOwner(id));
        }
        return new ModelAndView("cottage/allMyCottages");
    }

    @GetMapping("/{id}")
    public ModelAndView showCottage(@PathVariable("id") Long id, Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("cottage", this.cottageService.findById(id));
        return new ModelAndView("cottage/cottage");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/{id}/edit")
    public ModelAndView edit(Model model, @PathVariable Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        Cottage cottage = this.cottageService.findById(id);
        model.addAttribute("cottage", cottage);

        Collection<Cottage> cottages = this.cottageService.findByCottageOwner(id);
        model.addAttribute("cottages", cottages);

        return new ModelAndView("cottage/editCottage");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/allMyCottages/{id}/editCottage/{cid}")
    public ModelAndView updateCottage(Model model, @PathVariable("id") Long id,
                                      @PathVariable("cid") Long cid) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        Cottage cottage = this.cottageService.findById(cid);
        model.addAttribute("cottage", cottage);

        Collection<Cottage> cottages = this.cottageService.findByCottageOwner(id);
        model.addAttribute("cottages", cottages);
        return new ModelAndView("cottage/editCottage");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @PostMapping("/{id}/edit/submit")
    public ModelAndView edit(@PathVariable Long id, Model model, Cottage cottage,
                             @RequestParam("image") MultipartFile image) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        Collection<Cottage> cottages = this.cottageService.findByCottageOwner(id);
        model.addAttribute("cottages", cottages);

        cottage.setCottageOwner((CottageOwner) this.userService.getUserFromPrincipal());

        Path path = Paths.get("C:\\Users\\Dijana\\Desktop\\Cottages\\cottages\\uploads");
        try {
            InputStream inputStream = image.getInputStream();
            Files.copy(inputStream, path.resolve(image.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            cottage.setImageUrl(image.getOriginalFilename().toLowerCase());
            model.addAttribute("cottage", cottage);
            model.addAttribute("imageUrl", cottage.getImageUrl());

        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean update = this.cottageService.canUpdateOrDelete(id);
        if (!update) {
            return new ModelAndView("cottage/errors/errorUpdateCottage");
        } else {
            this.cottageService.updateCottage(cottage);
        }

        return new ModelAndView("redirect:/cottages/{id}/");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping(value = "/allMyCottages/{id}/remove/{cid}")
    public ModelAndView removeCottage(@PathVariable Long cid,
                                      @PathVariable Long id,
                                      Model model) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        Cottage cottage = this.cottageService.findById(cid);

        boolean delete = this.cottageService.canUpdateOrDelete(cid);
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
    }

    @GetMapping("/allCottages")
    public ModelAndView getAllCottages(Model model, String keyword) throws Exception {
        if (keyword != null) {
            model.addAttribute("cottages", this.cottageService.findByKeyword(keyword));
        } else {
            model.addAttribute("cottages", this.cottageService.findAll());
        }

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("cottage/cottages");
        } catch (Exception e) {
            //System.out.println("error all cottages");
            return new ModelAndView("cottagesGuests");
        }
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/{id}/defineAvailability")
    public ModelAndView defineAvailability(Model model, @PathVariable Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        Cottage cottage = this.cottageService.findById(id);
        model.addAttribute("cottage", cottage);

        return new ModelAndView("cottage/defineAvailability");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @PostMapping("/{id}/defineAvailability/submit")
    public ModelAndView defineAvailability(Model model, @PathVariable Long id, @ModelAttribute Cottage cottage) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        Collection<Cottage> cottages = this.cottageService.findByCottageOwner(id);
        model.addAttribute("cottages", cottages);
        model.addAttribute("cottage", cottage);

        cottage.setCottageOwner((CottageOwner) this.userService.getUserFromPrincipal());
        this.cottageService.defineAvailability(cottage);

        return new ModelAndView("redirect:/cottages/{id}/");
    }

    @GetMapping("/allCottages/sortByNameDesc")
    public ModelAndView sortByNameDesc(Model model) {
        List<Cottage> sorted = this.cottageService.orderByNameDesc();
        model.addAttribute("cottages", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("cottage/cottages");
        } catch (Exception e) {
            // return new ModelAndView("cottage/cottagesGuests");
            return new ModelAndView("home");
        }
    }

    @GetMapping("/allCottages/sortByNameAsc")
    public ModelAndView sortByNameAsc(Model model) {
        List<Cottage> sorted = this.cottageService.orderByNameAsc();
        model.addAttribute("cottages", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("cottage/cottages");
        } catch (Exception e) {
            // return new ModelAndView("cottage/cottagesGuests");
            return new ModelAndView("home");
        }
    }

    @GetMapping("/allCottages/sortByRatingDesc")
    public ModelAndView sortByRatingDesc(Model model) {
        List<Cottage> sorted = this.cottageService.orderByRatingDesc();
        model.addAttribute("cottages", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("cottage/cottages");
        } catch (Exception e) {
            // return new ModelAndView("cottage/cottagesGuests");
            return new ModelAndView("home");
        }
    }

    @GetMapping("/allCottages/sortByRatingAsc")
    public ModelAndView sortByRatingAsc(Model model) {
        List<Cottage> sorted = this.cottageService.orderByRatingAsc();
        model.addAttribute("cottages", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("cottage/cottages");
        } catch (Exception e) {
            // return new ModelAndView("cottage/cottagesGuests");
            return new ModelAndView("home");
        }
    }

    @GetMapping("/allCottages/SortByAddressAsc")
    public ModelAndView orderByAddressAsc(Model model) {
        List<Cottage> sorted = this.cottageService.orderByAddressAsc();
        model.addAttribute("cottages", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("cottage/cottages");
        } catch (Exception e) {
            // return new ModelAndView("cottage/cottagesGuests");
            return new ModelAndView("home");
        }
    }

    @GetMapping("/allCottages/SortByAddressDesc")
    public ModelAndView orderByAddressDesc(Model model) {
        List<Cottage> sorted = this.cottageService.orderByAddressDesc();
        model.addAttribute("cottages", sorted);

        try {
            model.addAttribute("principal", this.userService.getUserFromPrincipal());
            return new ModelAndView("cottage/cottages");
        } catch (Exception e) {
            // return new ModelAndView("cottage/cottagesGuests");
            return new ModelAndView("home");
        }
    }
}
