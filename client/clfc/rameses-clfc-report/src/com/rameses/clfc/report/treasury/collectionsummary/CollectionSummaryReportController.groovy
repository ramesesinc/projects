package com.rameses.clfc.report.treasury.collection;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class CollectionSummaryReportController extends ReportModel
{    
    @Service("CollectionSummaryReportService")
    def service;
    
    @Service("DateService")
    def dateSvc;

    String title = "Collection Summary Report";

    def mode = 'init'
    def startdate;
    def enddate;
    
    void init() {
        startdate = dateSvc.getServerDateAsString();
        enddate = startdate;
    }

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
        return "com/rameses/clfc/report/treasury/collectionsummary/CollectionSummaryReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/treasury/collectionsummary/CollectionSummaryReportDetail.jasper')
        ];
    }
}