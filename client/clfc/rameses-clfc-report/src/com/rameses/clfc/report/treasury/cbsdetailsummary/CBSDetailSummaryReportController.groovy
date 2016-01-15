package com.rameses.clfc.report.treasury.cbsdetailsummary;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class CBSDetailSummaryReportController extends ReportModel
{
    @Service("CBSDetailSummaryReportService")
    def service;

    String title = "CBS Detail Summary Report";

    def mode = 'init'
    def cbsno;
    def collector;
    def collectorLookup = Inv.lookupOpener("route-collector:lookup", [:]);

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
        return service.getReportData([cbsno: cbsno]);
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/treasury/cbsdetailsummary/CBSDetailSummaryReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('CASHBREAKDOWN', 'com/rameses/clfc/report/treasury/cbsdetailsummary/CBSDetailSummaryReportDetail.jasper')
        ];
    }
}