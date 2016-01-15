package com.rameses.clfc.report.controller;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class LoanapplicationReportController extends ReportModel
{
    @Service("LoanAppReportService")
    def service;
    
    String title = "Loan Application";
    def loanappid;
    
    def close() {
        return '_close';
    }
    
    void preview() {
        viewReport();
    }

    public Map getParameters() {
        return [:];
    }

    public Object getReportData() {
        return service.getLoanapplicationReportData([loanappid: loanappid]);
    }

    public String getReportName() {
        return "com/rameses/clfc/report/loanapplication/LoanApplication.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('PROPERTYHOLDING', 'com/rameses/clfc/report/loanapplication/LoanAppPropertyHolding.jasper')
        ];
    }
}
