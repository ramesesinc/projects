package com.rameses.clfc.report.loan.masterlist

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class LoanMasterListReportController extends ReportModel 
{
    @Service("LoanMasterListReportService")
    def service;

    @Service("DateService")
    def dateSvc;
    
    String title = "Loan Master List";

    def mode = 'init'
    def startdate, enddate, criteria;
    def reportCriteriaList = [];

    void init() {
        startdate = dateSvc.getServerDateAsString();
        enddate = startdate;
        //reportCriteriaList = service.getCriteria();
    }
    
    def routecode;
    def getRouteList() {
        return service.getRoutes();
    }
    
    def categoryid;
    def getCategoryList() {
        return service.getCategories();
    }
    
    def close() {
        return "_close";
    }
    
    private def getParams() {
        return [
            routecode   : routecode,
            categoryid  : categoryid
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
        return "com/rameses/clfc/report/loan/masterlist/LoanMasterListReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/loan/masterlist/LoanMasterListReportDetail.jasper'),
            new SubReport('ROUTE_DETAIL', 'com/rameses/clfc/report/loan/masterlist/LoanMasterListReportRouteDetail.jasper'),
            new SubReport('CATEGORY_DETAIL', 'com/rameses/clfc/report/loan/masterlist/LoanMasterListReportCategoryDetail.jasper')
        ];
    }
}

