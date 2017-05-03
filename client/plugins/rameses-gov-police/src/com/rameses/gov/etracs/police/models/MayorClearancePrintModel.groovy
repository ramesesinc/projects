package com.rameses.gov.etracs.police.models; 

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class MayorClearancePrintModel extends ReportController {

    def entity;
    def data = [:];
    def title = "Mayor's Clearance"

    String reportpath = "com/rameses/gov/police/reports/"
    String reportName = reportpath + "mayors-clearance.jasper";

    @FormTitle
    def formTitle

    def applicationid;

    def getReportData(){
        return data;
    }

    def printReport() {
        applicationid = entity.objid;
        return preview(); 
    } 

    def openReport() {
        formTitle = entity.txnno;
        applicationid = entity.applicationid;
        preview(); 
        return null; 
    } 
}   