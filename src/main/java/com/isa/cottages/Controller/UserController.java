package com.isa.cottages.Controller;

import com.isa.cottages.Exception.ResourceConflictException;
import com.isa.cottages.Model.*;
import com.isa.cottages.Service.impl.BoatOwnerServiceImpl;
import com.isa.cottages.Service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    private UserServiceImpl userService;
    private BoatOwnerServiceImpl boatOwnerService;

    @Autowired
    public UserController(UserServiceImpl userService, BoatOwnerServiceImpl boatOwnerService) {
        this.userService = userService;
        this.boatOwnerService = boatOwnerService;
    }

    @GetMapping("/index")
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'COTTAGE_OWNER', 'BOAT_OWNER', 'CLIENT')")
    public ModelAndView indexPage(Authentication auth) throws Exception {
        User u = this.userService.findByEmail(auth.getName());
        if (u.getEnabled() == false) {
            throw new Exception("Your account is not activated, please check your email.");
        }
        // TODO: add separate page redirection for each user role
        /*if (u instanceof Client) {
            return new ModelAndView("client-home");
        }*/
        return new ModelAndView("indexPage");
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'COTTAGE_OWNER', 'BOAT_OWNER', 'CLIENT')")
    public User loadById(@PathVariable Long userId) {
        return this.userService.findById(userId);
    }

    @GetMapping("cottage-owner/home")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView cottageOwnerHome(Model model, Authentication auth) {
        CottageOwner cottageOwner = (CottageOwner) this.userService.findByEmail(auth.getName());
        model.addAttribute("user", cottageOwner);
        return new ModelAndView("cottage/myHome");
    }

    @GetMapping("sys-admin/home")
    @PreAuthorize("hasRole('SYS_ADMIN')")
    public ModelAndView sysAdminHome(Model model) {
        return new ModelAndView("sys-admin-home");
    }

    @GetMapping("/boat-owner/home")
    @PreAuthorize("hasRole('BOAT_OWNER')")
    public ModelAndView boatOwnerHome(Model model, Authentication auth) {
        BoatOwner boatOwner = (BoatOwner) userService.findByEmail(auth.getName());
        model.addAttribute("user", boatOwner);
        return new ModelAndView("boat/myHome");
    }

    @GetMapping("/client/home")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView clientHome(Model model, Authentication auth){
        Client client = (Client) userService.findByEmail(auth.getName());
        model.addAttribute("user", client);
        return new ModelAndView("client/home");
    }


    @GetMapping("/registerSystemAdmin")
    @PreAuthorize("hasRole('SYS_ADMIN')")
    public ModelAndView regSysAdminForm(Model model) {
        SystemAdministrator systemAdministrator = new SystemAdministrator();
        model.addAttribute(systemAdministrator);
        return new ModelAndView("registerSystemAdmin");
    }

    @PostMapping(value = "/registerSystemAdmin/submit")
    @PreAuthorize("hasRole('SYS_ADMIN')")
    public ModelAndView registerSystemAdmin(@ModelAttribute SystemAdministrator user) {
        if (this.userService.findByEmail(user.getEmail()) != null) {
            throw new ResourceConflictException(user.getId(), "Email already exists.");
        }
        this.userService.saveSystemAdmin(user);
        return new ModelAndView("redirect:/user/sys-admin/home");
    }
}
