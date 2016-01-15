package com.rameses.clfc.report.treasury.dailycollection;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class DailyCollectionCashierReportController extends ReportModel
{
    @Service("DailyCollectionCashierReportService")
    def service;

    String title = "Daily Collection Report (Cashier)";

    def mode = 'init'
    def txndate;

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
        return service.getReportData([txndate: txndate]);
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/treasury/dailycollection/DailyCollectionCashierReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('DETAIL', 'com/rameses/clfc/report/treasury/dailycollection/DailyCollectionReportDetail.jasper')
        ];
    }

}