package com.rameses.clfc.report.encashment;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class EncashmentCBSReportController extends ReportModel
{
    @Service("EncashmentCBSReportService")
    def service;

    String title = "Encashment Breakdown Report";

    def entity;

    def close() {
        return "_close";
    }

    void preview() {
        viewReport();
    }
    public Map getParameters() {
        return [:];
    }

    public Object getReportData() {
        return service.getReportData([objid: entity.cbsid]);
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/cashbreakdown/CashBreakdownReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('CASHBREAKDOWN', 'com/rameses/clfc/report/cashbreakdown/CashBreakdownReportDetail.jasper')
        ];
    }
}