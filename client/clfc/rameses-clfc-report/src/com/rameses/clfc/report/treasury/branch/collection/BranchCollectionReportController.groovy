package com.rameses.clfc.report.treasury.branch.collection

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class BranchCollectionReportController extends ReportModel
{
    @Service("BranchCollectionReportService")
    def service;

    @Service("DateService")
    def dateSvc;
    
    String title = "Branch Collection Report";

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
    
    def preview() {
        mode = 'preview';
        service.generate(getParams());
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
        return service.getReportData(getParams());
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/treasury/branch/loanrelease/BranchLoanReleaseReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/treasury/branch/loanrelease/BranchLoanReleaseReportDetail.jasper'),
            new SubReport('ROUTE_DETAIL', 'com/rameses/clfc/report/treasury/branch/loanrelease/BranchLoanReleaseReportRouteDetail.jasper')
        ];
    }
}

