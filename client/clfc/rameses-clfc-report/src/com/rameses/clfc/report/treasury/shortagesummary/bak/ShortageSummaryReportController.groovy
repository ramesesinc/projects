package com.rameses.clfc.report.treasury.shortagesummary;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class ShortageSummaryReportController extends ReportModel
{    
    @Service("ShortageSummaryReportService")
    def service;
    
    @Service('DateService')
    def dateSvc;

    String title = "Shortage Summary Report";

    def mode = 'init'
    def startdate;
    def enddate;
    
    void init() {
        startdate = dateSvc.getServerDateAsString().split(' ')[0];
        enddate = startdate;
    }

    def close() {
        return "_close";
    }
    
    def parseDate( date ) {
        if (!date) return null;
        
        if (date instanceof Date) {
            return date;
        } else {
            return java.sql.Date.valueOf(date);
        }
    }
    
    def preview() {
        def sd = parseDate(startdate);
        def ed = parseDate(enddate);
        if (ed.compareTo(sd) < 0)
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
        return "com/rameses/clfc/report/treasury/shortagesummary/ShortageSummaryReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/treasury/shortagesummary/ShortageSummaryReportDetail.jasper'),
            new SubReport('ITEMDETAIL', 'com/rameses/clfc/report/treasury/shortagesummary/ShortageSummaryReportItemDetail.jasper')
        ];
    }
}