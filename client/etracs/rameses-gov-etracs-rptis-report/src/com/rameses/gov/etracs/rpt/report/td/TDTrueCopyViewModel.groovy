package com.rameses.gov.etracs.rpt.report.td;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;
import net.sf.jasperreports.engine.*;

public class TDTrueCopyViewModel extends ReportModel
{
    def entity;
    def inv;
    String entityName = 'tdtruecopy:view';


    def view() {
        inv = Inv.lookupOpener('tdtruecopy:open', [entity: entity]);
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