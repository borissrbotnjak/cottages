package com.isa.cottages.Service.impl;

import com.isa.cottages.Repository.ComplaintRepository;
import com.isa.cottages.Repository.ReportRepository;
import com.isa.cottages.Service.SystemAdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemAdministratorServiceImpl implements SystemAdministratorService {
    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ReportRepository reportRepository;

}
