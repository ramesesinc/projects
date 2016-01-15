package com.rameses.clfc.report.collection.nac

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class CollectionNACReportController extends ReportModel
{
    @Binding
    def binding;
    
    @Service('CollectionNACReportService')
    def service;
    
    @Service('DateService')
    def dateSvc;
    
    String title = "NAC Report";

    
    def startdate, enddate, route, collector;    
    def mode = 'init'
    
    void init() {
        def date = dateSvc.getServerDateAsString().split(" ")[0];
        startdate = date;
        enddate = date;
        mode = 'init';
    }
    
    def getRouteList() {
        def params = [
            startdate   : startdate,
            enddate     : enddate
        ]
        return service.getRoutes(params);
    }
    
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
        def params = [
            startdate       : startdate,
            enddate         : enddate,
            route           : route,
            collector       : collector
        ];
        return service.getReportData(params);
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/collection/nac/CollectionNACReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/collection/nac/CollectionNACReportDetail.jasper'),
            new SubReport('DATE_DETAIL', 'com/rameses/clfc/report/collection/nac/CollectionNACReportDateDetail.jasper')
        ];
    }
}

