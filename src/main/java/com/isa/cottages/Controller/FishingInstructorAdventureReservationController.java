package com.isa.cottages.Controller;

import com.isa.cottages.Email.EmailSender;
import com.isa.cottages.Model.*;
import com.isa.cottages.Service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/adventureReservations")
public class FishingInstructorAdventureReservationController {
    private final FishingInstructorAdventureReservationServiceImpl reservationService;
    private final EmailSender emailSender;
    private final UserServiceImpl userService;
    private final FishingInstructorAdventureServiceImpl adventureService;
    private final ReportServiceImpl reportService;
    private final ClientServiceImpl clientService;

    @Autowired
    public FishingInstructorAdventureReservationController(EmailSender emailSender, FishingInstructorAdventureServiceImpl adventureService,
                                                           UserServiceImpl userService, FishingInstructorAdventureReservationServiceImpl reservationService,
                                                           ReportServiceImpl reportService, ClientServiceImpl clientService) {
        this.emailSender = emailSender;
        this.adventureService = adventureService;
        this.userService = userService;
        this.reservationService = reservationService;
        this.reportService = reportService;
        this.clientService = clientService;
    }

    //<editor-fold desc="Get upcoming reservations">
    @GetMapping("/upcomingInstructorsReservations/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ModelAndView upcomingReservations(Model model, @PathVariable Long id) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", instructor);

        model.addAttribute("adventureReservations", this.reservationService.getInstructorsUpcomingReservations(id));

        return new ModelAndView("instructor/upcomingReservations");
    }
    //</editor-fold>

    //<editor-fold desc="Get reservation history">
    @GetMapping("/pastInstructorsReservations/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ModelAndView showReservationHistory(Model model, String keyword, @PathVariable("id") Long id, String email) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", instructor);
        if (keyword != null) {
            model.addAttribute("adventureReservations", this.reservationService.findClient(keyword));
        } else {
            model.addAttribute("adventureReservations", this.reservationService.getInstructorsPastReservations(id));
        }
        return new ModelAndView("instructor/pastReservations");
    }
    //</editor-fold>

    //<editor-fold desc="Get all discounts and get/post define discount">
    @GetMapping("/allDiscounts/{id}")
    public ModelAndView getDiscountsByAdventure(@PathVariable Long id, Model model) throws Exception {
        User user = this.userService.getUserFromPrincipal();
        model.addAttribute("principal", user);

        FishingInstructorAdventure adventure = this.adventureService.findById(id);
        model.addAttribute("adventure", adventure);

        if(adventure == null) {
            throw new Exception("Adventure with this id does not exist.");
        }
        model.addAttribute("adventureReservations", reservationService.findDiscountsByAdventure(id));

        return new ModelAndView("instructor/allDiscounts");
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/{id}/defineDiscount")
    public ModelAndView defineDiscount(@PathVariable Long id, Model model) throws Exception {
        User user = this.userService.getUserFromPrincipal();
        model.addAttribute("principal", user);

        model.addAttribute("adventure", this.adventureService.findById(id));
        AdventureReservation adventureReservation = new AdventureReservation();

        model.addAttribute("adventureReservation", adventureReservation);

        Collection<AdventureReservation> adventureReservations = this.reservationService.findDiscountsByAdventure(id);
        model.addAttribute("adventureReservations", adventureReservations);

        return new ModelAndView("instructor/defineDiscount");
    }
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/{id}/defineDiscount/submit")
    public ModelAndView defineDiscount(@PathVariable Long id, @ModelAttribute AdventureReservation adventureReservation,
                                       Model model, String email) throws Exception {

        Collection<AdventureReservation> adventureReservations = this.reservationService.findDiscountsByAdventure(id);
        model.addAttribute("adventureReservations", adventureReservations);

        User user = this.userService.getUserFromPrincipal();
        model.addAttribute("principal", user);

        adventureReservation.setInstructor((Instructor) this.userService.getUserFromPrincipal());
        adventureReservation.setAdventure(this.adventureService.findById(id));
        adventureReservation.setDiscount(true);
        this.reservationService.saveDiscount(adventureReservation);

        FishingInstructorAdventure adventure = adventureService.findById(id);
        Set<Client> clients = adventure.getSubscribers();
        for (Client c:clients) {
            if (adventure.getSubscribers() != null && c.getAdventureSubscriptions() != null) {
                emailSender.send(c.getEmail(), email(c.getFirstName(), "New discount for adventure ", adventure.getAdventureName(), " published."));
            }
        }
        return new ModelAndView("redirect:/adventureReservations/allDiscounts/{id}/");
    }

    //</editor-fold>

    //<editor-fold desc="email sender">
    public String email(String name, String text1, String adventureName, String text2) {
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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:10px;line-height:25px;color:#0b0c0c\"> <p>" + text1 + adventureName + text2 +"</p> </p></blockquote>\n" +
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
    //</editor-fold>

    //<editor-fold desc="Get/post write report">
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/writeReport/{iid}/{clid}/{aid}")
    public ModelAndView reportForm(Model model, @PathVariable Long iid,
                                   @PathVariable Long clid, @PathVariable Long aid) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", instructor);
        model.addAttribute("advetntureReservations", this.reservationService.getInstructorsPastReservations(iid));
        Report report = new Report();

        model.addAttribute("report", report);
        model.addAttribute("clid", clid);
        model.addAttribute("aid", aid);
        model.addAttribute("iid", iid);

        return new ModelAndView("instructor/report");
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/writeReport/{iid}/{clid}/{aid}/submit")
    public ModelAndView reportFormSubmit(Model model,
                                         @PathVariable Long iid, @PathVariable Long clid,
                                         @PathVariable Long aid, @ModelAttribute Report report) throws Exception {

        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        report.setClient((Client) this.userService.findById(clid));
        report.setAdmin((SystemAdministrator) this.userService.findById(aid));
        report.setInstructor(instructor);
        report= this.reportService.save(report);
        model.addAttribute("principal", instructor);
        model.addAttribute("adventureReservations", this.reservationService.getInstructorsPastReservations(iid));
        model.addAttribute("clid", clid);
        model.addAttribute("aid", aid);
        model.addAttribute("iid", iid);
        model.addAttribute("report", report);

        if(report.getPenal() == Boolean.TRUE) {
            report.setApproved(Boolean.FALSE);
            this.reportService.update(report);
        }
        if (report.getDidAppear() == Boolean.FALSE) {
            report.getClient().setPenalties(report.getClient().getPenalties() + 1);
            this.reportService.update(report);
        }

        reportService.update(report);
        return new ModelAndView("redirect:/adventureReservations/pastInstructorsReservations/{iid}");
    }
    //</editor-fold>

    //<editor-fold desc="Get make reservation with client">

    @GetMapping("/{iid}/makeReservationWithClient")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ModelAndView showAvailableClients(Model model, @PathVariable Long iid) throws Exception {

        model.addAttribute("clients", this.clientService.findAllAvailable_Adventure(iid));
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        return new ModelAndView("instructor/makeReservation/showAvailableClients");
    }
    //</editor-fold>

    //<editor-fold desc="get available adventures sorted ">
    @GetMapping("/{iid}/{clid}/showAvailableAdventures")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ModelAndView showAvailableAdventures(Model model, @PathVariable Long iid,
                                              @PathVariable Long clid,
                                              @RequestParam("startDate") String startDate,
                                              @RequestParam("endDate") String endDate,
                                              @RequestParam("numPersons") Integer numPersons) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        Client client = (Client) userService.findById(clid);
        model.addAttribute("clid", clid);
        model.addAttribute("client",client);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate sd = LocalDate.parse(startDate, formatter);
        LocalDate ed = LocalDate.parse(endDate, formatter);
        model.addAttribute("startDate", sd);
        model.addAttribute("endDate", ed);
        model.addAttribute("numPersons", numPersons);
        model.addAttribute("instructor", this.userService.findById(iid));

        model.addAttribute("adventures", this.adventureService.findAllMyAvailable(sd, ed, numPersons, iid));

        return new ModelAndView("instructor/makeReservation/showAvailableAdventures");
    }

    @GetMapping("/{iid}/{clid}/showAvailableAdventures/byPriceAsc")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ModelAndView showAvailableSortedByPriceAsc(Model model, @PathVariable Long clid, @PathVariable Long iid,
                                                      @RequestParam("startDate") String startDate,
                                                      @RequestParam("endDate") String endDate,
                                                      @RequestParam("numPersons") Integer numPersons) throws Exception {
        model.addAttribute("principal", userService.getUserFromPrincipal());
        Client client = (Client) userService.findById(clid);
        model.addAttribute("clid", clid);
        model.addAttribute("iid", iid);
        model.addAttribute("client", userService.findById(clid));
        model.addAttribute("instructor", userService.findById(iid));
        model.addAttribute("client",client);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate sd = LocalDate.parse(startDate, formatter);
        LocalDate ed = LocalDate.parse(endDate, formatter);

        model.addAttribute("startDate", sd);
        model.addAttribute("endDate", ed);
        model.addAttribute("numPersons", numPersons);

        model.addAttribute("adventures", this.adventureService.findAllMyAvailableSorted(iid, sd, ed, numPersons, true, true, false));

        return new ModelAndView("instructor/makeReservation/showAvailableAdventures");
    }

    @GetMapping("/{iid}/{clid}/showAvailableAdventures/byPriceDesc")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ModelAndView showAvailableSortedByPriceDesc(Model model, @PathVariable Long iid,
                                                       @PathVariable Long clid,
                                                       @RequestParam("startDate") String startDate,
                                                       @RequestParam("endDate") String endDate,
                                                       @RequestParam("numPersons") Integer numPersons) throws Exception {
        model.addAttribute("principal", userService.getUserFromPrincipal());
        Client client = (Client) userService.findById(clid);
        model.addAttribute("clid", clid);
        model.addAttribute("client",client);
        model.addAttribute("iid", iid);
        model.addAttribute("client", userService.findById(clid));
        model.addAttribute("instructor", userService.findById(iid));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate sd = LocalDate.parse(startDate, formatter);
        LocalDate ed = LocalDate.parse(endDate, formatter);

        model.addAttribute("startDate", sd);
        model.addAttribute("endDate", ed);
        model.addAttribute("numPersons", numPersons);

        model.addAttribute("adventures", this.adventureService.findAllMyAvailableSorted(iid, sd, ed, numPersons, false, true, false));
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        return new ModelAndView("instructor/makeReservation/showAvailableAdventures");
    }

    @GetMapping("/{iid}/{clid}/showAvailableAdventures/byRatingAsc")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ModelAndView showAvailableSortedByRatingAsc(Model model, @PathVariable long iid,
                                                       @PathVariable Long clid,
                                                       @RequestParam("startDate") String startDate,
                                                       @RequestParam("endDate") String endDate,
                                                       @RequestParam("numPersons") Integer numPersons) throws Exception {
        model.addAttribute("principal", userService.getUserFromPrincipal());
        Client client = (Client) userService.findById(clid);
        model.addAttribute("clid", clid);
        model.addAttribute("client",client);
        model.addAttribute("iid", iid);
        model.addAttribute("client", userService.findById(clid));
        model.addAttribute("instructor", userService.findById(iid));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate sd = LocalDate.parse(startDate, formatter);
        LocalDate ed = LocalDate.parse(endDate, formatter);

        model.addAttribute("startDate", sd);
        model.addAttribute("endDate", ed);
        model.addAttribute("numPersons", numPersons);

        model.addAttribute("adventures", this.adventureService.findAllMyAvailableSorted(iid, sd, ed, numPersons, true, false, true));

        return new ModelAndView("instructor/makeReservation/showAvailableAdventures");
    }

    @GetMapping("/{iid}/{clid}/showAvailableAdventures/byRatingDesc")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ModelAndView showAvailableSortedByRatingDesc(Model model, @PathVariable Long iid,
                                                        @PathVariable Long clid,
                                                        @RequestParam("startDate") String startDate,
                                                        @RequestParam("endDate") String endDate,
                                                        @RequestParam("numPersons") Integer numPersons) throws Exception {
        model.addAttribute("principal", userService.getUserFromPrincipal());
        Client client = (Client) userService.findById(clid);
        model.addAttribute("clid", clid);
        model.addAttribute("client",client);
        model.addAttribute("iid", iid);
        model.addAttribute("client", userService.findById(clid));
        model.addAttribute("instructor", userService.findById(iid));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate sd = LocalDate.parse(startDate, formatter);
        LocalDate ed = LocalDate.parse(endDate, formatter);

        model.addAttribute("startDate", sd);
        model.addAttribute("endDate", ed);
        model.addAttribute("numPersons", numPersons);

        model.addAttribute("adventures", this.adventureService.findAllMyAvailableSorted(iid, sd, ed, numPersons, false, false, true));

        return new ModelAndView("instructor/makeReservation/showAvailableAdventures");
    }
    //</editor-fold>

    //<editor-fold desc="Discount cancellation and calendar">
    @GetMapping("/onDiscount/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView showOffersOnDiscount(@PathVariable("id") Long id, Model model) throws Exception {
        model.addAttribute("reservations", this.reservationService.getAllWithDiscount(id));
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        return new ModelAndView("instructor/reservationsOnDiscount");
    }

    @PostMapping("/onDiscount/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView selectOffersOnDiscount(@PathVariable("id") Long id, Model model) throws Exception {
        Client client = (Client) this.userService.getUserFromPrincipal();
        AdventureReservation reservation = this.reservationService.makeReservationOnDiscount(id);

        model.addAttribute("principal", client);
        return new ModelAndView("redirect:/adventureReservations/success");
    }

    @RequestMapping("/cancel/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView cancelReservation(@PathVariable Long id, Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        if (this.reservationService.canCancel(id)) {
            this.reservationService.cancel(id);
            return new ModelAndView("redirect:/adventureReservations/upcoming");
        }

        return new ModelAndView("reservation/cancellationError");
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/viewCalendar/{id}")
    public ModelAndView viewCalendar (Model model, @PathVariable Long id, String keyword) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", instructor);
        if (keyword != null) {
            model.addAttribute("adventureReservations", this.reservationService.findClient(keyword));
        } else {
            model.addAttribute("adventureReservations", this.reservationService.getAllInstructorsNowAndUpcomingReservations(id));
        }
        return new ModelAndView("instructor/calendar");
    }
    //</editor-fold>

    //<editor-fold desc="Choose Date groupation">
    @GetMapping("/{id}/chooseDate")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ModelAndView chooseDate(Model model, @PathVariable Long id) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        model.addAttribute("startDate", LocalDate.now());
        model.addAttribute("endDate", LocalDate.now());

        return new ModelAndView("instructor/reports/chooseDate2");
    }

    @PostMapping("/{id}/chooseDate2")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ModelAndView chooseDate2(Model model, @PathVariable Long id,
                                    @RequestParam("startDate") String startDate,
                                    @RequestParam("endDate") String endDate) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate ld1 = LocalDate.parse(startDate, formatter);
        LocalDate ld2 = LocalDate.parse(endDate, formatter);

        model.addAttribute("startDate", ld1);
        model.addAttribute("endDate", ld2);

        return new ModelAndView("redirect:/adventureReservations/{id}/incomes");
    }

    @GetMapping("/{id}/incomes")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ModelAndView incomes (Model model, @RequestParam("startDate") String startDate,
                                 @RequestParam("endDate") String endDate, @PathVariable Long id) throws Exception{
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ld1 = LocalDate.parse(startDate, formatter);
        LocalDate ld2 = LocalDate.parse(endDate, formatter);
        model.addAttribute("startDate", ld1);
        model.addAttribute("endDate", ld2);

        Set<AdventureReservation> reservations = this.reservationService.findByInterval(ld1, ld2, id);
        model.addAttribute("reservations", reservations);

        Double income = 0.0;
        for(AdventureReservation cr: reservations) {
            income += cr.getPrice();
        }
        model.addAttribute("income", income);

        return new ModelAndView("instructor/reports/incomes");
    }

    @GetMapping("/{id}/chooseDate3")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ModelAndView chooseDate3(Model model, @PathVariable Long id) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        model.addAttribute("startDate", LocalDate.now());
        model.addAttribute("endDate", LocalDate.now());

        return new ModelAndView("instructor/reports/chooseDate3");
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/{id}/chooseDate4")
    public ModelAndView chooseDate4(Model model, @PathVariable Long id,
                                    @RequestParam("startDate") String startDate,
                                    @RequestParam("endDate") String endDate) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate ld1 = LocalDate.parse(startDate, formatter);
        LocalDate ld2 = LocalDate.parse(endDate, formatter);

        model.addAttribute("startDate", ld1);
        model.addAttribute("endDate", ld2);

        return new ModelAndView("redirect:/adventureReservations/{id}/attendance");
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/{id}/attendance")
    public ModelAndView reportOfAttendance(Model model, @PathVariable Long id,
                                           @RequestParam("startDate") String startDate,
                                           @RequestParam("endDate") String endDate) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ld1 = LocalDate.parse(startDate, formatter);
        LocalDate ld2 = LocalDate.parse(endDate, formatter);
        model.addAttribute("startDate", ld1);
        model.addAttribute("endDate", ld2);

        Set<AdventureReservation> reservations = this.reservationService.findByInterval2(ld1, ld2, id);
        model.addAttribute("reservations", reservations);
        Integer attendance = reservations.size();

        model.addAttribute("attendance", attendance);


        return new ModelAndView("instructor/reports/attendance");
    }


    @GetMapping("/{iid}/{clid}/selectClient")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ModelAndView selectClient(@PathVariable Long iid,
                                     @PathVariable Long clid,
                                     Model model) throws Exception {
        model.addAttribute("principal", userService.getUserFromPrincipal());

        Client client = (Client) userService.findById(clid);
        model.addAttribute("clid", clid);
        model.addAttribute("client",client);

        return new ModelAndView("redirect:/adventureReservations/{iid}/{clid}/next");
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/{iid}/{clid}/next")
    public ModelAndView chooseDate(@PathVariable Long iid, @PathVariable Long clid,
                                   Model model) throws Exception {
        model.addAttribute("principal", userService.getUserFromPrincipal());

        Client client = (Client) userService.findById(clid);
        model.addAttribute("clid", clid);
        model.addAttribute("client",client);

        model.addAttribute("startDate", LocalDate.now());
        model.addAttribute("endDate", LocalDate.now());
        model.addAttribute("numPersons", 1);

        return new ModelAndView("instructor/makeReservation/chooseDate");
    }

    @PostMapping("/{iid}/{clid}/chooseDate")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ModelAndView chooseDate(Model model, @PathVariable Long iid,
                                   @PathVariable Long clid,
                                   @RequestParam("startDate") String startDate,
                                   @RequestParam("endDate") String endDate,
                                   @RequestParam("numPersons") Integer numPersons) throws Exception {
        model.addAttribute("principal", userService.getUserFromPrincipal());

        Client client = (Client) userService.findById(clid);
        model.addAttribute("clid", clid);
        model.addAttribute("client",client);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate sd = LocalDate.parse(startDate, formatter);
        LocalDate ed = LocalDate.parse(endDate, formatter);
        model.addAttribute("startDate", sd);
        model.addAttribute("endDate", ed);
        model.addAttribute("numPersons", numPersons);

        return new ModelAndView("redirect:/adventureReservations/{iid}/{clid}/showAvailableAdventures");
    }
    //</editor-fold>

    //<editor-fold desc="choose date time and entity">
    @GetMapping("/chooseTime")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView chooseDate(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        model.addAttribute("startDate", LocalDate.now());
        model.addAttribute("endDate", LocalDate.now());
        model.addAttribute("numPersons", 1);
        model.addAttribute("res_type", "adventure");

        return new ModelAndView("reservation/chooseTime");//???
    }

    @PostMapping("/chooseTime")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView chooseTime(Model model, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
                                   @RequestParam("numPersons") Integer numPersons) throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate sd = LocalDate.parse(startDate, formatter);
        LocalDate ed = LocalDate.parse(endDate, formatter);

        model.addAttribute("startDate", sd);
        model.addAttribute("endDate", ed);
        model.addAttribute("numPersons", numPersons);

        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        return new ModelAndView("redirect:/adventureReservations/available");
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView showAvailable(Model model, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
                                      @RequestParam("numPersons") Integer numPersons) throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate sd = LocalDate.parse(startDate, formatter);
        LocalDate ed = LocalDate.parse(endDate, formatter);

        model.addAttribute("startDate", sd);
        model.addAttribute("endDate", ed);
        model.addAttribute("numPersons", numPersons);

        model.addAttribute("adventures", this.adventureService.findAllAvailable(sd, ed, numPersons));
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        return new ModelAndView("instructor/available");
    }

    @GetMapping("/select/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView selectEntity(@PathVariable Long id, Model model, @RequestParam("startDate") String startDate,
                                     @RequestParam("endDate") String endDate, @RequestParam("numPersons") Integer numPersons) throws Exception {

        Client client = (Client) this.userService.getUserFromPrincipal();

        model.addAttribute("principal", client);
        model.addAttribute("services", this.adventureService.findById(id).getAdditionalServices());
        model.addAttribute("entity_id", id);
        model.addAttribute("startDateString", startDate);
        model.addAttribute("endDateString", endDate);
        model.addAttribute("numPersons", numPersons);

        AdventureReservation reservation = new AdventureReservation();
        model.addAttribute("reservation", reservation);
        model.addAttribute("res_type", "adventure");
        model.addAttribute("sLength", this.adventureService.findById(id).getAdditionalServices().size());

        return new ModelAndView("additionalServices");
    }
    //</editor-fold>

    //<editor-fold desc="Get reservation history & upcoming(client) and sort past reservations">
    @GetMapping("/history")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView showReservationHistory(Model model, String keyword) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        if (keyword != null) {
            // TODO: Dodaj pretragu
            //model.addAttribute("reservations", this.reservationService.findByKeyword(keyword));
        } else {
            model.addAttribute("adventureReservations", this.reservationService.getPastReservations());
        }
        // TODO: dodaj stranicu
        return new ModelAndView("instructor/pastReservations");
    }

    @GetMapping("/upcoming")
    @PreAuthorize("hasRole('CLIENT')")
    public ModelAndView showUpcomingReservations(Model model, String keyword) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        if (keyword != null) {
            // TODO: Dodaj pretragu
            //model.addAttribute("reservations", this.reservationService.findByKeyword(keyword));
        } else {
            model.addAttribute("adventreReservations", this.reservationService.getUpcomingReservations());
        }
        // TODO: Dopuni stranicu
        return new ModelAndView("instructor/upcomingReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByDateAsc")
    public ModelAndView sortPastReservationsByDateAsc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("adventureReservations", this.reservationService.findByOrderByStartTimeAsc());

        return new ModelAndView("adventure/pastReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByDateDesc")
    public ModelAndView sortPastReservationsByDateDesc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("adventureReservations", this.reservationService.findByOrderByStartTimeDesc());

        return new ModelAndView("adventure/pastReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByDurationAsc")
    public ModelAndView sortPastReservationsByDurationAsc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("adventureReservations", this.reservationService.findByOrderByDurationAsc());

        return new ModelAndView("adventure/pastReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByDurationDesc")
    public ModelAndView sortPastReservationsByDurationDesc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("adventureReservations", this.reservationService.findByOrderByDurationDesc());

        return new ModelAndView("adventure/pastReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByPriceAsc")
    public ModelAndView sortPastReservationsByPriceAsc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("adventureReservations", this.reservationService.findByOrderByPriceAsc());

        return new ModelAndView("adventure/pastReservations");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history/sortByPriceDesc")
    public ModelAndView sortPastReservationsByPriceDesc(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());
        model.addAttribute("adventureReservations", this.reservationService.findByOrderByPriceDesc());

        return new ModelAndView("adventure/pastReservations");
    }
    //</editor-fold>

    //<editor-fold desc="Make reservation">
    @GetMapping("/{iid}/selectAdventure/{clid}/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ModelAndView selectAdventure(@PathVariable Long id, @PathVariable Long clid,
                                      @PathVariable Long iid,
                                      Model model, @RequestParam("startDate") String startDate,
                                      @RequestParam("endDate") String endDate,
                                      @RequestParam("numPersons") Integer numPersons) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        Client client = (Client) userService.findById(clid);
        model.addAttribute("client", client);
        model.addAttribute("clid", clid);

        model.addAttribute("services", this.adventureService.findById(id).getAdditionalServices());
        model.addAttribute("adventure_id", id);
        model.addAttribute("startDateString", startDate);
        model.addAttribute("endDateString", endDate);
        model.addAttribute("numPersons", numPersons);

        AdventureReservation reservation = new AdventureReservation();
        model.addAttribute("reservation", reservation);
        model.addAttribute("sLength", this.adventureService.findById(id).getAdditionalServices().size());

        return new ModelAndView("instructor/makeReservation/showAdditionalServices");
    }

    @PostMapping("/{iid}/reserve/{adventureId}/{clid}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ModelAndView makeReservation(@PathVariable("adventureId") Long adventureId,
                                        @PathVariable Long clid,
                                        @PathVariable Long iid,
                                        Model model,
                                        @ModelAttribute("reservation") AdventureReservation reservation) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        FishingInstructorAdventure adventure = this.adventureService.findById(adventureId);
        AdventureReservation res = this.reservationService.makeReservationWithClient(reservation, adventure, clid);

        Client client = (Client) userService.findById(clid);
        model.addAttribute("client", client);

        model.addAttribute("reservation", res);
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        emailSender.send(client.getEmail(), emailSuccess(
                client.getFirstName(),"You successfully made an adventure reservation. ",
                "Adventure: ", adventure.getAdventureName(),
                "Instructor: ", reservation.getInstructor().getFullName(),
                "Reservation start: ", reservation.getStartDate(),
                "Reservation end: ", reservation.getEndDate(),
                "Number of guests: ", reservation.getNumPersons(),
                "Price: ", reservation.getPrice()
        ));

        return new ModelAndView("redirect:/adventureReservations/end");
    }

    @GetMapping("/end")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ModelAndView reservationConfirmation(Model model) throws Exception {
        model.addAttribute("principal", this.userService.getUserFromPrincipal());

        return new ModelAndView("instructor/makeReservation/success");
    }

    public String emailSuccess(String name, String text1, String text2, String adventureName, String text3,
                               String instructorName,
                               String text4, LocalDate startDate, String text5, LocalDate endDate,
                               String text6, Integer numPersons, String text7, Double price) {
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
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Reservation</span>\n" +
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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:10px;line-height:25px;color:#0b0c0c\"> <p>" + text1
                + "</br>" + text2 + adventureName + ", "
                + "</br>" + text3 + instructorName + ", "
                + "</br>" + text4 + startDate + ", "
                + "</br>" + text5 + endDate + ", "
                + "</br>" + text6 + numPersons + ", "
                + "</br>" + text7 + price
                + "</p> </p></blockquote>\n" +
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
    //</editor-fold>

}
