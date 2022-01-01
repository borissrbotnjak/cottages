package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.Report;
import com.isa.cottages.Repository.ReportRepository;
import com.isa.cottages.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    private ReportRepository reportRepository;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public Report findById(Long id) throws Exception {

        if(this.reportRepository.getById(id) == null) {
            throw new Exception("Report with this id does not exist");
        }
        return this.reportRepository.getById(id);
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

        return this.reportRepository.save(r);
    }
}
