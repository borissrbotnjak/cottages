package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.*;
import com.isa.cottages.Repository.ReportRepository;
import com.isa.cottages.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private ReportRepository reportRepository;
    private UserServiceImpl userService;
    private ClientServiceImpl clientService;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository, UserServiceImpl userService, ClientServiceImpl clientService) {
        this.reportRepository = reportRepository;
        this.userService = userService;
        this.clientService = clientService;
    }

    @Override
    public Report findById(Long id) throws Exception {

        return this.reportRepository.findById(id).get();
    }

    @Override
    public Report givePenalty(Long rid)  throws Exception
    {
        Report report = this.findById(rid);
        Client client = (Client) this.userService.findById(report.getClient().getId());
        if (client.getPenalties() == null)
            client.setPenalties(0);
        client.setPenalties(report.getClient().getPenalties() + 1);
        report.setApproved(Boolean.TRUE);
        report= this.update(report);
        this.clientService.updateProfile(client);

        return report;
    }

    public List<Report> findNotApprovedReports()throws Exception
    {
        return this.reportRepository.findReportByApprovedIsFalse();
    }

    @Override
    public List<Report> findBoatOwnersReports(Long id) throws Exception {
        BoatOwner boatOwner = (BoatOwner) this.userService.getUserFromPrincipal();

        return this.reportRepository.findBoatOwnersReports(id);
    }

    @Override
    public List<Report> findCottageOwnersReports(Long id) throws Exception {
        CottageOwner cottageOwner = (CottageOwner) this.userService.getUserFromPrincipal();

        return this.reportRepository.findCottageOwnersReports(id);
    }

    @Override
    public List<Report> findInstructorsReports(Long id) throws Exception {
        Instructor instructor = (Instructor) this.userService.getUserFromPrincipal();

        return this.reportRepository.findInstructorsReports(id);
    }
    @Override
    public Report update(Report report) throws Exception {
        Report r = findById(report.getId());
        r.setText(report.getText());
        r.setPenal(report.getPenal());
        r.setDidAppear(report.getDidAppear());
        r.setBoatOwner(report.getBoatOwner());
        r.setAdmin(report.getAdmin());
        r.setClient(report.getClient());
        r.getClient().setPenalties(report.getClient().getPenalties());
        r.setApproved(report.getApproved());
        this.reportRepository.save(r);
        return r;
    }

    @Override
    public Report save(Report report) throws Exception {
        Report r = new Report();
        r.setText(report.getText());
        r.setPenal(report.getPenal());
        r.setDidAppear(report.getDidAppear());
        r.setBoatOwner(report.getBoatOwner());
        r.setAdmin(report.getAdmin());
        r.setClient(report.getClient());
        if(report.getClient().getPenalties()==null)
            r.getClient().setPenalties(0);
        else
            r.getClient().setPenalties(report.getClient().getPenalties());
        r.setApproved(report.getApproved());

        return this.reportRepository.save(r);
    }
}
