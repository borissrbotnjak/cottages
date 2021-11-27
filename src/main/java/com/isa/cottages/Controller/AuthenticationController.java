package com.isa.cottages.Controller;

import com.isa.cottages.DTO.ChangePasswordAfterFirstLoginDTO;
import com.isa.cottages.DTO.ChangePasswordDTO;
import com.isa.cottages.DTO.UserDTO;
import com.isa.cottages.Exception.ResourceConflictException;
import com.isa.cottages.Model.*;
import com.isa.cottages.Service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

    private final UserServiceImpl userService;

    @Autowired
    public AuthenticationController(UserServiceImpl userService)
    {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView loginForm(Model model, String email) {
        User user1 = userService.findByEmail(email);
        UserDTO user = new UserDTO();
//        UserRequest userRequest = new UserRequest();
//        model.addAttribute("userRequest", userRequest);
        model.addAttribute("user", user);
        return new ModelAndView("login");
    }

    @GetMapping("/home")
    public ModelAndView home() {
        return new ModelAndView("home");
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return new ModelAndView("redirect:/auth/home");
    }

    @GetMapping("/signup")
    public ModelAndView registrationForm(Model model){
        UserRequest userRequest = new UserRequest();
        model.addAttribute(userRequest);
        return new ModelAndView("client/registration");
    }

    @PostMapping("/signup/submit")
    public ModelAndView addClient(@ModelAttribute("userRequest") @Valid UserRequest userRequest, BindingResult result) {

        User existUser = this.userService.findByEmail(userRequest.getEmail());
        if (existUser != null) {
            throw new ResourceConflictException(userRequest.getId(), "Email already exists");
        }
        if (result.hasErrors()){
            return new ModelAndView("redirect:/auth/signup");
        }
        this.userService.saveClient(userRequest);

        return new ModelAndView("redirect:/auth/home");
    }


    @GetMapping("/signupOwner")
    public ModelAndView registrationOwnerForm(Model model){
        UserRequest userRequest = new UserRequest();
        model.addAttribute(userRequest);
        return new ModelAndView("cottage/registration");
    }

    @PostMapping("/signupOwner/submit")
    public ModelAndView addOwner(@ModelAttribute("userRequest") @Valid UserRequest userRequest,
                                 BindingResult result, @ModelAttribute User user) {
        User existUser = this.userService.findByEmail(userRequest.getEmail());
        if (existUser != null) {
            throw new ResourceConflictException(userRequest.getId(), "Email already exists.");
        }
        if (result.hasErrors()) {
            return new ModelAndView("redirect:/auth/signupOwner");
        }

        userRequest.setRegistrationType(userRequest.getRegistrationType());
//        userService.saveCottageOwner(userRequest);
        if (userRequest.getRegistrationType() == RegistrationType.COTTAGE_ADVERTISER) {
            userService.saveCottageOwner(userRequest);
            user.setUserRole(UserRole.COTTAGE_OWNER);
            user.setRegistrationType(RegistrationType.COTTAGE_ADVERTISER);
        } else if (userRequest.getRegistrationType() == RegistrationType.BOAT_ADVERTISER) {
            userService.saveBoatOwner(userRequest);
            user.setUserRole(UserRole.BOAT_OWNER);
            user.setRegistrationType(RegistrationType.BOAT_ADVERTISER);
        }
        return new ModelAndView("redirect:/auth/home");
    }

    @GetMapping("/signupInstructor")
    public ModelAndView registrationInstructorForm(Model model) {
        UserRequest userRequest = new UserRequest();
        model.addAttribute(userRequest);
        return new ModelAndView("cottage/registration");
    }

    @PostMapping("/signupInstructor/submit")
    public ModelAndView addInstructor(@ModelAttribute("userRequest") @Valid UserRequest userRequest, BindingResult result) {
        User existUser = this.userService.findByEmail(userRequest.getEmail());
        if (existUser != null) {
            throw new ResourceConflictException(userRequest.getId(), "Email already exists.");
        }
        if (result.hasErrors()) {
            return new ModelAndView("redirect:/auth/signupInstructor");
        }
        this.userService.saveInstructor(userRequest);
        userRequest.setRegistrationType(userRequest.getRegistrationType());
        return new ModelAndView("redirect:/auth/home");
    }

    //Promena lozinke nakon prvog prijavljivanja za Administratora
    @GetMapping("/change-password-first")
    public ModelAndView changePasswordForm(Model model) {
        ChangePasswordAfterFirstLoginDTO changePasswordAfterFirstLoginDTO = new ChangePasswordAfterFirstLoginDTO();
        model.addAttribute(changePasswordAfterFirstLoginDTO);
        return new ModelAndView("change-password-first");
    }

    @PostMapping("/change-password-first/submit")
    public ModelAndView cps(@ModelAttribute("changePasswordAfterFirstLoginDTO")@Valid ChangePasswordAfterFirstLoginDTO changePasswordAfterFirstLoginDTO, Authentication auth, BindingResult bindingResult){

        User user = this.userService.findByEmail(auth.getName());
        this.userService.changePasswordAfterFirstLogin(user,changePasswordAfterFirstLoginDTO);
        if (bindingResult.hasErrors()){
            return new ModelAndView("redirect:/auth/change-password-first");
        }
        if(user instanceof SystemAdministrator){
            return new ModelAndView("redirect:/user/sys-admin/home");
        } else {
            return new ModelAndView("redirect:/auth/home");
        }
    }

    //Promena lozinke za ostale korisnike
    @GetMapping("/change-password")
    public ModelAndView changePasswordForm2(Model model){
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        model.addAttribute(changePasswordDTO);
        return new ModelAndView("change-password");
    }

    @PostMapping("/change-password/submit")
    public ModelAndView cps2(@ModelAttribute("changePasswordDTO")@Valid ChangePasswordDTO changePasswordDTO, Authentication auth, BindingResult bindingResult){

        User user = this.userService.findByEmail(auth.getName());
        this.userService.changePassword(user,changePasswordDTO);
        if (bindingResult.hasErrors()){
            return new ModelAndView("redirect:/auth/change-password");
        }
        if(user instanceof SystemAdministrator){
            return new ModelAndView("redirect:/user/sys-admin/home");
        } else if (user instanceof CottageOwner){
            return new ModelAndView("redirect:/user/cottage-owner/home");
        } else if (user instanceof BoatOwner) {
            return new ModelAndView("redirect:/user/boat-owner/home");
        } else if(user instanceof Client){
            return new ModelAndView("redirect:/user/client/home");
        }
        else {
            return new ModelAndView("redirect:/auth/home");
        }
    }

    @GetMapping("confirm")
    public String confirm(@RequestParam("token") String token){
        return userService.confirmToken(token);
    }

    @PreAuthorize("hasAnyRole('COTTAGE_OWNER', 'CLIENT', 'BOAT_OWNER')")
    @GetMapping("/deleteAccount")
    public ModelAndView deleteAccount(Model model) throws Exception {
        User user = this.userService.getUserFromPrincipal();
        model.addAttribute("principal", user);

        Request request = new Request();
        model.addAttribute("request", request);

        return new ModelAndView("deleteAccount");
    }

    @PreAuthorize("hasAnyRole('COTTAGE_OWNER', 'CLIENT', 'BOAT_OWNER')")
    @PostMapping("/deleteAccount/submit")
    public ModelAndView deleteAccountSubmit(Model model, @ModelAttribute Request request) throws Exception {
        User user = this.userService.getUserFromPrincipal();
        model.addAttribute("principal", user);

        if(user.getUserRole() == UserRole.COTTAGE_OWNER) {
            return new ModelAndView("redirect:/cottageOwner/profile/" + user.getId());
        }
        return new ModelAndView("home");
    }
}