package com.rameses.gov.etracs.sre.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.util.*;

class SREAbstractOfCollectionReportModel extends com.rameses.osiris2.reports.ReportController {
    
    @Service("SREAbstractOfCollectionReportService")
    def svc;
        
    String title = "SRE Abstract of Collection";
    String reportPath = "com/rameses/gov/etracs/sre/report";
    String reportName = reportPath + "/sre_abstractofcollection_crosstab.jasper";
    
    def data = [:];
    def funds;      
    def fund;

    def acctgroups;
    def acctgroup;
    
    def initReport() {
        def m = svc.initReport([ objid: entity.objid ]); 
        funds = m.funds; 
        acctgroups = m.acctgroups; 

        this.fund = (funds ? funds.first() : [:]);
        this.acctgroup = (acctgroups ? acctgroups.first() : [:]);
        return preview();
    }
    
    def getReportData() {
        def m = [ objid: entity.objid ];
        m.fund = this.fund; 
        m.acctgroup = this.acctgroup; 
        
        def res = svc.getReport( m ); 
        data.clear(); 
        data.putAll( res ); 
        return data.items;
    }

    Map getParameters(){
        return data?.header; 
    }    
}