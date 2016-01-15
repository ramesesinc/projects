package com.rameses.clfc.report.controller;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class ComakerStatementReportController extends ReportModel
{
    @Service("LoanAppReportService")
    def service;
    
    @Service("ComakerService")
    def comakerSvc;
    
    String title = "Co-Maker's Statement"
    def loanappid;
    def comaker;
    def mode = 'init';
    def comakers = [];
    
    void init() {
        def loanapp = comakerSvc.open([objid: loanappid])
        comakers = loanapp.comakers;
        comakers.each{
            it.name = it.lastname+', '+it.firstname+' '+(it.middlename? it.middlename : '');
        }
    }
    
    def close() {
        return '_close';
    }
    
    def back() {
        mode = 'init';
        return 'default';
    }
    
    def preview() {
        mode = 'view'
        viewReport();
        return 'preview';
    }

    public Map getParameters() {
        return [:];
    }

    public Object getReportData() {
        return service.getComakerStatementReportData([loanappid: loanappid, comakerid: comaker.objid]);
    }

    public String getReportName() {
        return "com/rameses/clfc/report/comakerstatement/CoMakerStatement.jasper";
    }
}
