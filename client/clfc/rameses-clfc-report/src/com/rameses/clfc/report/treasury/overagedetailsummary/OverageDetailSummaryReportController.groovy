package com.rameses.clfc.report.treasury.overagedetailsummary;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class OverageDetailSummaryReportController extends ReportModel
{
    @Service("OverageDetailSummaryReportService")
    def service;

    String title = "Overage Detail Summary Report";

    def mode = 'init'
    def refno;
    def verifier;
    def signatoryLookup = Inv.lookupOpener("loan-signatory:lookup", [:]);

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
        return service.getReportData([refno: refno, verifier: verifier]);
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/treasury/overagedetailsummary/OverageDetailSummaryReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            //new SubReport('CASHBREAKDOWN', 'com/rameses/clfc/report/treasury/shortagedetailsummary/CBSDetailSummaryReportDetail.jasper')
        ];
    }
}