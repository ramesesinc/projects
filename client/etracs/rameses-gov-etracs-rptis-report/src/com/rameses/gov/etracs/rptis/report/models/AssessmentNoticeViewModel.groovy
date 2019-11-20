package com.rameses.gov.etracs.rptis.report.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;
import net.sf.jasperreports.engine.*;

public class AssessmentNoticeViewModel extends ReportModel
{
    @Service('RPTAssessmentNoticeService')
    def svc;

    def entity;
    def inv;
    String entityName = 'assessmentnotice:view';


    void doView() {
        entity.putAll(svc.getReportData(entity));
        inv = Inv.lookupOpener('assessmentnotice:report', [entity: entity]);
        viewReport();
    } 

    public Object getReportData() {
        return inv.handle.report.getReportData();
    }

    public String getReportName() {
        return inv.handle.report.getReportName();
    }

    public SubReport[] getSubReports() {
        return inv.handle.report.getSubReports();
    }

    public Map getParameters() { 
        return inv.handle.report.getParameters();
    }

}