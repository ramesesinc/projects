package com.rameses.clfc.report.overage

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class OverageReportController extends ReportModel
{	
    @Service("OverageDetailSummaryReportService")
    def service;

    String title = "Overage Report";
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
        return service.getReportDataByOverageid([objid: entity.objid]);
    }

    public String getReportName() {
        return "com/rameses/clfc/report/overage/OverageReport.jasper";
    }
}