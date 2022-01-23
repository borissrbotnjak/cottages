package com.isa.cottages.Service;

import com.isa.cottages.Model.Report;

import java.util.List;

public interface ReportService {

    Report findById(Long id) throws Exception;

    Report givePenalty(Long rid)  throws Exception;

    List<Report> findBoatOwnersReports(Long id) throws Exception;
    List<Report> findCottageOwnersReports(Long id) throws Exception;

    List<Report> findInstructorsReports(Long id) throws Exception;

    Report update(Report report) throws Exception;

    Report save(Report report) throws Exception;
}
