package com.isa.cottages.Controller;

import com.isa.cottages.Model.Client;
import com.isa.cottages.Service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@AllArgsConstructor
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private UserServiceImpl userService;

    @GetMapping("/boat")
    private ModelAndView getAllBoat(Model model) throws Exception {
        Client client = (Client) this.userService.getUserFromPrincipal();
        model.addAttribute("subscriptions", client.getBoatSubscriptions());
        model.addAttribute("principal", client);
        return new ModelAndView("subscription/boat");
    }

    @GetMapping("/cottage")
    private ModelAndView getAllCottage(Model model) throws Exception {
        Client client = (Client) this.userService.getUserFromPrincipal();
        model.addAttribute("subscriptions", client.getCottageSubscriptions());
        model.addAttribute("principal", client);
        return new ModelAndView("subscription/cottage");
    }

    @GetMapping("/instructor")
    private ModelAndView getAllInstructor(Model model) throws Exception {
        Client client = (Client) this.userService.getUserFromPrincipal();
        model.addAttribute("subscriptions", client.getInstructorSubscriptions());
        model.addAttribute("principal", client);
        return new ModelAndView("subscription/instructor");
    }
}
