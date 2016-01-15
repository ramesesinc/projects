package com.rameses.clfc.report.treasury.branch

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class BranchReportController extends ReportModel
{
    @Service("BranchReportService")
    def service;

    @Service("DateService")
    def dateSvc;
    
    String title = "Branch Summary Report";

    def mode = 'init'
    def startdate, enddate, criteria;
    def reportCriteriaList = [];

    void init() {
        startdate = dateSvc.getServerDateAsString();
        enddate = startdate;
        reportCriteriaList = service.getCriteria();
    }
    
    def close() {
        return "_close";
    }
    
    def preview() {
        mode = 'preview';
        viewReport();
        return 'preview';
    }
    
    def back() {
        mode = 'init';
        return "default";
    }
    public Map getParameters() {
        return [:];
    }

    public Object getReportData() {
        return service.getReportData([startdate: startdate, enddate: enddate, criteria: criteria]);
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/amnesty/expired/ExpiredAmnestySummaryReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/amnesty/expired/ExpiredAmnestySummaryReportDetail.jasper')
        ];
    }
}

