package com.rameses.clfc.report.shortage;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class ShortageReportController extends ReportModel
{
    @Service("ShortageDetailSummaryReportService")
    def service;

    String title = "Shortage Report";
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
        return service.getReportDataByShortageid([objid: entity.objid]);
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/shortage/ShortageReport.jasper";
    }

}