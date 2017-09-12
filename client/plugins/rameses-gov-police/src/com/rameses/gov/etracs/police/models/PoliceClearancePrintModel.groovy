package com.rameses.gov.etracs.police.models; 

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class PoliceClearancePrintModel extends ReportController {

    @Service('PoliceClearanceReportService')
    def reportSvc; 
    
    def entity;
    def data = [:];
    def title = "Police Clearance"

    String reportpath = "com/rameses/gov/police/reports/"
    String reportName = reportpath + "police-clearance.jasper";
    
    def ctx = com.rameses.rcp.framework.ClientContext.currentContext; 

    @FormTitle
    def getFormTitle() {
        return entity.controlno; 
    }

    def getReportData() { 
        if ( data ) {
            def appenv = ctx.appEnv; 
            data.jsonurlpath = "http://"+ appenv['app.host']+'/'+appenv['app.cluster']+'/json'; 
            data.apps1 = [ data.app ]; 
            data.apps2 = [ data.app ]; 
            data.ctcs1 = [ data.ctc ]; 
            data.ctcs2 = [ data.ctc ]; 
            data.applicants1 = [ data.applicant ]; 
            data.applicants2 = [ data.applicant ]; 
        } 
        return data; 
    }

    def printReport() {
        data = entity; 
        return preview(); 
    } 

    def openReport() { 
        data = reportSvc.getPoliceClearance([ clearanceid: entity.objid ]); 
        return preview(); 
    } 
}   