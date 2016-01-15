package com.rameses.clfc.report.loan.expiries

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class LoanExpiriesReportController extends ReportModel
{   
    @Service("LoanExpiriesReportService")
    def service;

    @Service("DateService")
    def dateSvc;
    
    String title = "Loan Expiries";

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
        //service.generate(getParams());
        rptdata = service.getReportData(getParams());
        viewReport();
        mode = 'preview';
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
        return rptdata;//service.getReportData(getParams());
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/loan/expiries/LoanExpiriesSummaryReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/loan/expiries/LoanExpiriesSummaryReportDetail.jasper')
        ];
    }
}

