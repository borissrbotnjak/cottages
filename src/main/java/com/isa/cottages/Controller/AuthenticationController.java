package com.isa.cottages.Controller;

import com.isa.cottages.DTO.ChangePasswordAfterFirstLoginDTO;
import com.isa.cottages.DTO.UserDTO;
import com.isa.cottages.Exception.ResourceConflictException;
import com.isa.cottages.Model.SystemAdministrator;
import com.isa.cottages.Model.User;
import com.isa.cottages.Model.UserRequest;
import com.isa.cottages.Service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    private UserServiceImpl userService;

    @Autowired
    public AuthenticationController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView loginForm(Model model) {
        UserDTO user = new UserDTO();
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

    @GetMapping("/signupOwner")
    public ModelAndView registrationOwnerForm(Model model){
        UserRequest userRequest = new UserRequest();
        model.addAttribute(userRequest);
        return new ModelAndView("registrationOwner");
    }

    @PostMapping("/signupOwner/submit")
    public ModelAndView addUser(@ModelAttribute("userRequest") @Valid UserRequest userRequest, BindingResult result) {
        User existUser = this.userService.findByEmail(userRequest.getEmail());
        if (existUser != null) {
            throw new ResourceConflictException(userRequest.getId(), "Email already exists.");
        }
        if (result.hasErrors()) {
            return new ModelAndView("redirect:/auth/signupOwner");
        }
        //Provera da li je oznacio da je RegistrationType == COTTAGE_ADVERTISER/BOAT_ADVERTISER
        this.userService.saveCottageOwner(userRequest);

        return new ModelAndView("redirect:/auth/home");
    }

    @GetMapping("/change-password")
    public ModelAndView changePasswordForm(Model model){
        ChangePasswordAfterFirstLoginDTO changePasswordAfterFirstLoginDTO = new ChangePasswordAfterFirstLoginDTO();
        model.addAttribute(changePasswordAfterFirstLoginDTO);
        return new ModelAndView("change-password");
    }

    @PostMapping("/change-password/submit")
    public ModelAndView cps(@ModelAttribute("changePasswordAfterFirstLoginDTO")@Valid ChangePasswordAfterFirstLoginDTO changePasswordAfterFirstLoginDTO, Authentication auth, BindingResult bindingResult){

        User user = this.userService.findByEmail(auth.getName());
        this.userService.changePasswordAfterFirstLogin(user,changePasswordAfterFirstLoginDTO);
        if (bindingResult.hasErrors()){
            return new ModelAndView("redirect:/auth/change-password");
        }
        if(user instanceof SystemAdministrator){
            return new ModelAndView("redirect:/user/sys-admin/home");
        } else {
            return new ModelAndView("redirect:/auth/home");
        }
    }
}
