package com.rameses.gov.etracs.police.models; 

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class MayorClearancePrintModel extends ReportController {

    @Service('PoliceClearanceReportService') 
    def reportSvc; 
    
    def ctx = com.rameses.rcp.framework.ClientContext.currentContext; 
    
    def entity;
    def data = [:];
    def title = "Mayor's Clearance"

    String reportpath = "com/rameses/gov/police/reports/"
    String reportName = reportpath + "mayors-clearance.jasper";

    @FormTitle
    def getFormTitle() {
        return data.txnno; 
    }

    def applicationid;

    def getReportData(){ 
        if ( data ) {
            def appenv = ctx.appEnv; 
            data.jsonurlpath = "http://"+ appenv['app.host']+'/'+appenv['app.cluster']+'/json';             
        }
        return data;
    }

    def printReport() { 
        data = entity;
        return preview(); 
    } 

    def openReport() {
        data = reportSvc.getMayorClearance([ clearanceid: entity.objid ]); 
        return preview(); 
    } 
}   