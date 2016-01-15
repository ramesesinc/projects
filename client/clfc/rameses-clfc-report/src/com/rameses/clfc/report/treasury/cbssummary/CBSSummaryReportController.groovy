package com.rameses.clfc.report.treasury.cbssummary;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class CBSSummaryReportController extends ReportModel
{    
    @Service("CBSSummaryReportService")
    def service;

    String title = "CBS Summary Report";

    def mode = 'init'
    def startdate;
    def enddate;

    def close() {
        return "_close";
    }
    
    def preview() {
        if (java.sql.Date.valueOf(enddate).compareTo(java.sql.Date.valueOf(startdate)) < 0)
            throw new Exception("End date must not be less than start date.");

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
        return service.getReportData([startdate: startdate, enddate: enddate]);
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/treasury/cbssummary/CBSSummaryReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/treasury/cbssummary/CBSSummaryReportDetail.jasper'),
            new SubReport('ITEMDETAIL', 'com/rameses/clfc/report/treasury/cbssummary/CBSSummaryReportItemDetail.jasper')
        ];
    }
}