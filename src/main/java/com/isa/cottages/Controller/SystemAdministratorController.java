package com.isa.cottages.Controller;

import com.isa.cottages.Email.EmailSender;
import com.isa.cottages.Model.*;
import com.isa.cottages.Service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping(value = "/sysadmin")
public class SystemAdministratorController {

    private final SystemAdministratorServiceImpl administratorService;
    private final ComplaintServiceImpl complaintService;
    private final ReportServiceImpl reportService;
    private final UserRequestServiceImpl requestService;
    private final UserServiceImpl userService;
    private final ClientServiceImpl clientService;
    private final EmailSender emailSender;

    @Autowired
    public SystemAdministratorController(SystemAdministratorServiceImpl administratorService, ComplaintServiceImpl complaintService, ReportServiceImpl reportService, UserRequestServiceImpl requestService, UserServiceImpl userService, ClientServiceImpl clientService, EmailSender emailSender) {
        this.administratorService = administratorService;
        this.complaintService = complaintService;
        this.reportService = reportService;
        this.requestService = requestService;
        this.userService = userService;
        this.clientService = clientService;
        this.emailSender = emailSender;
    }

    @PreAuthorize("hasRole('SYS_ADMIN')")
    @GetMapping("/userrequests")
    public ModelAndView seeRequests(Model model) throws Exception {
        SystemAdministrator systemAdministrator = (SystemAdministrator) this.userService.getUserFromPrincipal();
        List<UserRequest> notEnabled = this.requestService.findNotEnabled();
        model.addAttribute("principal", systemAdministrator);
        model.addAttribute("requests", notEnabled);
        return new ModelAndView("sysadmin/requests");
    }

    @PreAuthorize("hasRole('SYS_ADMIN')")
    @GetMapping("/{id}/acceptRequest/{rid}")
    public ModelAndView acceptRequest(@PathVariable Long id, @PathVariable Long rid,
                                      Model model) throws Exception {
        SystemAdministrator systemAdministrator = (SystemAdministrator) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", systemAdministrator);
        UserRequest userRequest = this.requestService.findById(rid);
        userRequest.setEnabled(Boolean.TRUE);
        this.userService.setEnabled(userRequest.getEmail());
        this.requestService.update(userRequest);
        Collection<UserRequest> requests = this.requestService.findNotEnabled();
        model.addAttribute("requests", requests);
        emailSender.send(userRequest.getEmail(), emailAcceptRequest(" you have been Accepted"));

        return new ModelAndView("redirect:/sysadmin/userrequests/");
    }

    public String emailAcceptRequest(String text) {
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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + text + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:10px;line-height:25px;color:#0b0c0c\"> <p> </p> </p></blockquote>\n" +
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

    @PreAuthorize("hasRole('SYS_ADMIN')")
    @GetMapping("/{id}/declineRequest/{rid}")
    public ModelAndView declineRequest(@PathVariable Long id, @PathVariable Long rid,
                                       Model model, UserRequest userRequest) throws Exception {
        SystemAdministrator systemAdministrator = (SystemAdministrator) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", systemAdministrator);

        UserRequest request = this.requestService.findById(rid);
        model.addAttribute("request", request);

        return new ModelAndView("sysadmin/declinationReason");
    }

    public String emailDeclineRequest(String text) {
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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi your request has been delined: " + text + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:10px;line-height:25px;color:#0b0c0c\"> <p> </p> </p></blockquote>\n" +
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

    @PreAuthorize("hasRole('SYS_ADMIN')")
    @GetMapping("/complaints")
    public ModelAndView seeComplaints(Model model) throws Exception {
        SystemAdministrator systemAdministrator = (SystemAdministrator) this.userService.getUserFromPrincipal();
        List<Complaint> notAnswered = this.complaintService.findNotAnswered();
        model.addAttribute("principal", systemAdministrator);
        model.addAttribute("complaints", notAnswered);
        return new ModelAndView("sysadmin/complaints");
    }

    @PreAuthorize("hasRole('SYS_ADMIN')")
    @GetMapping("/{id}/answerComplaint/{cid}")
    public ModelAndView answerComplaint(@PathVariable Long id, @PathVariable Long cid,
                                        Model model) throws Exception {
        SystemAdministrator administrator = (SystemAdministrator) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", administrator);

        Complaint complaint = this.complaintService.findById(cid);
        model.addAttribute("complaint", complaint);

        return new ModelAndView("sysadmin/answerComplaint");
    }

    @PreAuthorize("hasRole('SYS_ADMIN')")
    @PostMapping("/{id}/declineRequest/submit")
    public ModelAndView submitDeclination(@PathVariable Long id, Model model) throws Exception {
        SystemAdministrator systemAdministrator = (SystemAdministrator) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", systemAdministrator);
        UserRequest userRequest = this.requestService.findById(id);
        emailSender.send(userRequest.getEmail(), emailDeclineRequest(userRequest.getDeclinationReason()));
        this.userService.deleteByEmail(userRequest.getEmail());
        this.requestService.delete(userRequest);
        Collection<UserRequest> requests = this.requestService.findNotEnabled();

        model.addAttribute("requests", requests);
        return new ModelAndView("redirect:/sysadmin/userrequests/");
    }

    @PreAuthorize("hasRole('SYS_ADMIN')")
    @PostMapping("/{id}/edit/submit")
    public ModelAndView submitAnswer(@PathVariable Long id, Model model, Complaint complaint) throws Exception {


        SystemAdministrator systemAdministrator = (SystemAdministrator) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", systemAdministrator);
        complaint.setIsAnswered(Boolean.TRUE);
        this.complaintService.update(complaint);

        if (complaint.getInstructor() != null) {
            emailSender.send(complaint.getClient().getEmail(), email(complaint.getResponse()));
            emailSender.send(complaint.getInstructor().getInstructor().getEmail(), email(complaint.getResponse()));
        } else if (complaint.getBoat() != null) {
            emailSender.send(complaint.getClient().getEmail(), email(complaint.getResponse()));
            emailSender.send(complaint.getBoat().getBoatOwner().getEmail(), email(complaint.getResponse()));
        } else if (complaint.getCottage() != null) {
            emailSender.send(complaint.getClient().getEmail(), email(complaint.getResponse()));
            emailSender.send(complaint.getCottage().getCottageOwner().getEmail(), email(complaint.getResponse()));
        }
        Collection<Complaint> complaints = this.complaintService.findNotAnswered();
        model.addAttribute("complaints", complaints);
        return new ModelAndView("redirect:/sysadmin/complaints/");
    }

    public String email(String text) {
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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + text + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:10px;line-height:25px;color:#0b0c0c\"> <p> </p> </p></blockquote>\n" +
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

    @PreAuthorize("hasRole('SYS_ADMIN')")
    @GetMapping("/reports")
    public ModelAndView seeReports(Model model) throws Exception {
        SystemAdministrator systemAdministrator = (SystemAdministrator) this.userService.getUserFromPrincipal();
        List<Report> notApproved = this.reportService.findNotApprovedReports();
        model.addAttribute("principal", systemAdministrator);
        model.addAttribute("reports", notApproved);
        return new ModelAndView("sysadmin/reports");
    }

    @PreAuthorize("hasRole('SYS_ADMIN')")
    @GetMapping("/{id}/addPenal/{rid}")
    public ModelAndView addPenal(@PathVariable Long id, @PathVariable Long rid,
                                 Model model) throws Exception {
        SystemAdministrator systemAdministrator = (SystemAdministrator) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", systemAdministrator);
        Report report= this.reportService.givePenalty(rid);
        Client client = this.reportService.findById(rid).getClient();

        Collection<Report> reports = this.reportService.findNotApprovedReports();
        model.addAttribute("reports", reports);
        emailSender.send(report.getClient().getEmail(), email(" You have been penalized for" + report.getText()));
        if (report.getInstructor() != null)
            emailSender.send(report.getInstructor().getEmail(), email(" Client " + report.getClient().getFullName() + " has been penalized."));
        else if (report.getBoatOwner() != null)
            emailSender.send(report.getBoatOwner().getEmail(), email(" Client " + report.getClient().getFullName() + " has been penalized."));
        else if (report.getCottageOwner() != null)
            emailSender.send(report.getCottageOwner().getEmail(), email(" Client " + report.getClient().getFullName() + " has been penalized."));
        return new ModelAndView("redirect:/sysadmin/reports/");
    }

    @PreAuthorize("hasRole('SYS_ADMIN')")
    @GetMapping("/{id}/doNothing/{rid}")
    public ModelAndView doNothing(@PathVariable Long id, @PathVariable Long rid,
                                 Model model) throws Exception {
        SystemAdministrator systemAdministrator = (SystemAdministrator) this.userService.getUserFromPrincipal();
        model.addAttribute("principal", systemAdministrator);
        Report report = this.reportService.findById(rid);
        report.setApproved(Boolean.TRUE);
        report= this.reportService.update(report);
        Collection<Report> reports = this.reportService.findNotApprovedReports();
        model.addAttribute("reports", reports);
        emailSender.send(report.getClient().getEmail(), email(" You have been reported, but not penalized for" + report.getText()));
        if (report.getInstructor() != null)
            emailSender.send(report.getInstructor().getEmail(), email(" Client " + report.getClient().getFullName() + " has not been penalized."));
        else if (report.getBoatOwner() != null)
            emailSender.send(report.getBoatOwner().getEmail(), email(" Client " + report.getClient().getFullName() + " has not been penalized."));
        else if (report.getCottageOwner() != null)
            emailSender.send(report.getCottageOwner().getEmail(), email(" Client " + report.getClient().getFullName() + " has not been penalized."));
        return new ModelAndView("redirect:/sysadmin/reports/");
    }


}
