package com.isa.cottages.Service;

import com.isa.cottages.Model.Report;

public interface ReportService {

    Report findById(Long id) throws Exception;

    Report save(Report report) throws Exception;
}
