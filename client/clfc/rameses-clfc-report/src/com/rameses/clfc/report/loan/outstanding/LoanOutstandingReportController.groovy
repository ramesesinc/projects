package com.rameses.clfc.report.loan.outstanding

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osirsi2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class LoanOutstandingReportController extends ReportModel 
{
    @Service("LoanOutstandingReportService")
    def service;

    @Service("DateService")
    def dateSvc;
    
    String title = "Loan Outstanding Report";

    def mode = 'init'
    def startdate, enddate, criteria;
    def reportCriteriaList = [];

    void init() {
        startdate = dateSvc.getServerDateAsString();
        enddate = startdate;
        //reportCriteriaList = service.getCriteria();
    }
    
    def close() {
        return "_close";
    }
    
    private def getParams() {
        return [
            startdate   : startdate, 
            enddate     : enddate, 
            criteria    : criteria
        ];
    }
    
    def rptdata;
    def preview() {
        mode = 'preview';
        service.generate(getParams());
        rptdata = service.getReportData(getParams());
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
        return rptdata;
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/loan/outstanding/LoanOutstandingReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/loan/outstanding/LoanOutstandingReportDetail.jasper'),
            new SubReport('ROUTE_DETAIL', 'com/rameses/clfc/report/loan/outstanding/LoanOutstandingReportRouteDetail.jasper')
        ];
    }
}

