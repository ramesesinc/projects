package com.rameses.clfc.report.treasury.dailycollection;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class DailyCollectionReportController extends ReportModel
{
    @Binding
    def binding;
    
    @Service("DailyCollectionReportService")
    def service;

    @Service("DateService")
    def dateSvc;
    
    String title = "Daily Collection Report";

    def mode = 'init'
    def startdate, enddate, route, paymentmethod;
    def collectorList, collector, client, refno;
    
    def clientLookupHandler = Inv.lookupOpener("report-client:lookup", [:]);
    def paymentMethodList = [
        [value: 'schedule', caption: 'Schedule/Regular'],
        [value: 'over', caption: 'Overpayment']
    ]
    //def routeLookup = Inv.lookupOpener("route:lookup", [:]);

    void init() {
        def date = dateSvc.getServerDateAsString().split(" ")[0];
        startdate = date;
        enddate = date;
        collectorList = service.getCollectors([:]);
    }
    
    def getRouteList() {
        def params = [
            startdate   : startdate,
            enddate     : enddate
        ]
        return service.getRoutes(params);
    }
    /*
    def getRouteLookupHandler() {
        def params = [
            startdate   : startdate,
            enddate     : enddate
        ]
        return Inv.lookupOpener("report-route:lookup", params);
    }
    */
    
    def close() {
        return "_close";
    }
    
    def preview() {
        viewReport();
        mode = 'preview';
        return 'preview';
    }
    
    void clearClient() {
        client = null;
        binding.refresh("client");
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
            collector       : collector,
            client          : client,
            paymentmethod   : paymentmethod,
            refno           : refno
        ];
        return service.getReportData(params);
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/treasury/dailycollection/DailyCollectionReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/treasury/dailycollection/DailyCollectionReportDetail.jasper'),
            new SubReport('DATE_DETAIL', 'com/rameses/clfc/report/treasury/dailycollection/DailyCollectionReportDateDetail.jasper'),
            new SubReport('ROUTE_DETAIL', 'com/rameses/clfc/report/treasury/dailycollection/DailyCollectionReportRouteDetail.jasper'),
            new SubReport('ITEM_DETAIL', 'com/rameses/clfc/report/treasury/dailycollection/DailyCollectionReportDetailItem.jasper')
        ];
    }
}