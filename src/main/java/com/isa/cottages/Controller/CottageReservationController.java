package com.isa.cottages.Controller;

import com.isa.cottages.Email.EmailSender;
import com.isa.cottages.Model.*;
import com.isa.cottages.Service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/cottageReservations")
public class CottageReservationController {

    private CottageReservationServiceImpl reservationService;
    private UserServiceImpl userService;
    private CottageServiceImpl cottageService;
    private ReportServiceImpl reportService;
    private EmailSender emailSender;

    @Autowired
    public CottageReservationController(CottageReservationServiceImpl reservationService,
                                        UserServiceImpl userService,
                                        CottageServiceImpl cottageService,
                                        ReportServiceImpl reportService, EmailSender emailSender) {
        this.reservationService = reservationService;
        this.userService = userService;
        this.cottageService = cottageService;
        this.reportService = reportService;
        this.emailSender = emailSender;
    }

    @GetMapping("/upcomingOwnersReservations/{id}")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView showUpcomingReservations(Model model, @PathVariable Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        model.addAttribute("cottageReservations", this.reservationService.getOwnersUpcomingReservations(id));

        return new ModelAndView("cottage/upcomingReservations");
    }

    @GetMapping("/pastOwnersReservations/{id}")
    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    public ModelAndView showReservationHistory(Model model, String keyword, @PathVariable("id") Long id, String email) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);
//        Client client = this.clientService.findByEmail(email);
//        model.addAttribute("client", client);

        if (keyword != null) {
            model.addAttribute("cottageReservations", this.reservationService.findClient(keyword));
        } else {
            model.addAttribute("cottageReservations", this.reservationService.getOwnersPastReservations(id));
        }
        return new ModelAndView("cottage/pastReservations");
    }

    @GetMapping("/allDiscounts/{id}")
    public ModelAndView getDiscountsByCottage(@PathVariable Long id, Model model) throws Exception {
        User user =
                this.userService.getUserFromPrincipal();
        model.addAttribute("principal", user);

        Cottage cottage = this.cottageService.findById(id);
        model.addAttribute("cottage", cottage);

        if(cottage == null) {
            throw new Exception("Cottage with this id does not exist.");
        }
        model.addAttribute("cottageReservations", reservationService.findDiscountsByCottage(id));

        return new ModelAndView("cottage/allDiscounts");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/{id}/defineDiscount")
    public ModelAndView defineDiscount(@PathVariable Long id, Model model) throws Exception {
        User user = this.userService.getUserFromPrincipal();
        model.addAttribute("principal", user);

        model.addAttribute("cottage", this.cottageService.findById(id));
        CottageReservation cottageReservation = new CottageReservation();

        model.addAttribute("cottageReservation", cottageReservation);

        Collection<CottageReservation> cottageReservations = this.reservationService.findDiscountsByCottage(id);
        model.addAttribute("cottageReservations", cottageReservations);

        return new ModelAndView("cottage/defineDiscount");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @PostMapping("/{id}/defineDiscount/submit")
    public ModelAndView defineDiscount(@PathVariable Long id, @ModelAttribute CottageReservation cottageReservation,
                                       Model model, String email) throws Exception {
//        if (this.cottageService.findById(cottage.getId()) != null) {
//            throw new ResourceConflictException(cottage.getId(), "Cottage with this id already exist.");
//        }
        Collection<CottageReservation> cottageReservations = this.reservationService.findDiscountsByCottage(id);
        model.addAttribute("cottageReservations", cottageReservations);

        User user = this.userService.getUserFromPrincipal();
        model.addAttribute("principal", user);

        cottageReservation.setCottageOwner((CottageOwner) this.userService.getUserFromPrincipal());
        cottageReservation.setCottage(this.cottageService.findById(id));
        cottageReservation.setDiscount(true);
        this.reservationService.saveDiscount(cottageReservation);

        Cottage cottage = cottageService.findById(id);
        Client client = (Client) userService.findByEmail(email);
        if (cottage.getSubscriber() != null && client.getBoatSubscriptions() != null) {
            emailSender.send(client.getEmail(), email(client.getFirstName(), "New discount for cottage ", cottage.getName(), " published."));
        }
        return new ModelAndView("redirect:/cottageReservations/allDiscounts/{id}/");
    }

    public String email(String name, String text1, String cottageName, String text2) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">New discount:</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:10px;line-height:25px;color:#0b0c0c\"> <p>" + text1 + cottageName + text2 +"</p> </p></blockquote>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/writeReport/{oid}/{id}/{aid}")
    public ModelAndView reportForm(Model model, @PathVariable Long id,
                                   @PathVariable Long aid, @PathVariable Long oid) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);
        model.addAttribute("cottageReservations", this.reservationService.getOwnersPastReservations(oid));
        Report report = new Report();
        model.addAttribute("report", report);
        report.setClient((Client) this.userService.findById(id));
        report.setAdmin((SystemAdministrator) this.userService.findById(aid));
        model.addAttribute("id", id);
        model.addAttribute("aid", aid);
        model.addAttribute("oid", oid);

        return new ModelAndView("cottage/report");
    }


    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @PostMapping("/writeReport/{oid}/{id}/{aid}/submit")
    public ModelAndView reportFormSubmit(Model model, @ModelAttribute Report report,
                                         @PathVariable Long id, @PathVariable Long aid,
                                         @PathVariable Long oid,
                                         Client client) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);
        model.addAttribute("cottageReservations", this.reservationService.getOwnersPastReservations(oid));

        model.addAttribute("report", report);
        List<Report> reports = reportService.findCottageOwnersReports(cottageOwner.getId());
        model.addAttribute("reports", reports);
        SystemAdministrator admin = (SystemAdministrator) this.userService.findById(aid);
        model.addAttribute("id", id);
        model.addAttribute("aid", aid);
        model.addAttribute("oid", oid);

        report.setCottageOwner(cottageOwner);
        report.setClient((Client) this.userService.findById(id));
        report.setAdmin(admin);
        int penalties = 0;
        report.getClient().setPenalties(penalties);

        if(report.getPenal() == report.getPenal().TRUE) {
            admin.getReports().add(report);
            report.setApproved(true);
            penalties += 1;
            report.getClient().setPenalties(penalties);
        }
        if (report.getDidAppear() == report.getDidAppear().FALSE) {
            client.getPenalties();
            penalties += 1;
            report.getClient().setPenalties(penalties);
        }

        reportService.save(report);
        return new ModelAndView("redirect:/cottageReservations/pastOwnersReservations/{oid}");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/viewCalendar/{id}")
    public ModelAndView viewCalendar (Model model, @PathVariable Long id, String keyword) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);
        if (keyword != null) {
            model.addAttribute("cottageReservations", this.reservationService.findClient(keyword));
        } else {
            model.addAttribute("cottageReservations", this.reservationService.getAllOwnersUpcomingReservations(id));
        }
        return new ModelAndView("cottage/calendar");
    }

    @PreAuthorize("hasRole('COTTAGE_OWNER')")
    @GetMapping("/makeReservationWithClient")
    public ModelAndView makeReservationWithClient(Model model) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", cottageOwner);

        return new ModelAndView("cottage/makeReservationWithClient");
    }

        @GetMapping("/history")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView showReservationHistory(Model model, String keyword) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        if (keyword != null) {
            // TODO: Dodaj pretragu
            //model.addAttribute("reservations", this.reservationService.findByKeyword(keyword));
        } else {
            model.addAttribute("cottageReservations", this.reservationService.getPastReservations());
        }
        // TODO: dodaj stranicu
        return new ModelAndView("cottage/pastReservations");
    }

    @GetMapping("/upcoming")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView showUpcomingReservations(Model model, String keyword) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        if (keyword != null) {
            // TODO: Dodaj pretragu
            //model.addAttribute("reservations", this.reservationService.findByKeyword(keyword));
        } else {
            model.addAttribute("cottageReservations", this.reservationService.getUpcomingReservations());
        }
        // TODO: Dopuni stranicu
        return new ModelAndView("cottage/upcomingReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByDateAsc")
    public ModelAndView sortPastReservationsByDateAsc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("cottageReservations", this.reservationService.findByOrderByStartTimeAsc());

        return new ModelAndView("cottage/pastReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByDateDesc")
    public ModelAndView sortPastReservationsByDateDesc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("cottageReservations", this.reservationService.findByOrderByStartTimeDesc());

        return new ModelAndView("cottage/pastReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByDurationAsc")
    public ModelAndView sortPastReservationsByDurationAsc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("cottageReservations", this.reservationService.findByOrderByDurationAsc());

        return new ModelAndView("cottage/pastReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByDurationDesc")
    public ModelAndView sortPastReservationsByDurationDesc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("cottageReservations", this.reservationService.findByOrderByDurationDesc());

        return new ModelAndView("cottage/pastReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByPriceAsc")
    public ModelAndView sortPastReservationsByPriceAsc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("cottageReservations", this.reservationService.findByOrderByPriceAsc());

        return new ModelAndView("cottage/pastReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByPriceDesc")
    public ModelAndView sortPastReservationsByPriceDesc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("cottageReservations", this.reservationService.findByOrderByPriceDesc());

        return new ModelAndView("cottage/pastReservations");
    }
}
