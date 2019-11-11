package com.rameses.gov.etracs.landtax.report.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;
import net.sf.jasperreports.engine.*;

public class TaxClearanceViewModel extends ReportModel
{
    def entity;
    def inv;
    String entityName = 'rpttaxclearance:view';


    def view() {
        inv = Inv.lookupOpener('rpttaxclearance:open', [entity: entity]);
        viewReport();
        return 'default';
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