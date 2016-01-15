package com.rameses.clfc.report.controller;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class DemandLetterReportController extends ReportModel
{
    @Service("LoanAppReportService")
    def service;
    
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
        return service.getDemandLetterReportData([loanappid: loanappid]);
    }

    public String getReportName() {
        return "com/rameses/clfc/report/demandletter/DemandLetter.jasper";
    }
}
