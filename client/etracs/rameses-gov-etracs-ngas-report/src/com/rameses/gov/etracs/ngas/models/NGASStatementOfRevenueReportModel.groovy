package com.rameses.gov.etracs.ngas.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.util.*;

class NGASStatementOfRevenueReportModel extends com.rameses.osiris2.reports.ReportController {

    @Invoker
    def invoker;

    @Service("NGASStatementOfRevenueReportService")
    def svc;

    String title = "NGAS Statement of Revenue"; 
    String reportpath = "com/rameses/gov/etracs/ngas/report/";
    String reportName = reportpath;
    
    def data = [:];
    
    def funds;      
    def fund;

    def acctgroups;
    def acctgroup;
    def reftype;
    
    def initReport() {
        reftype = invoker?.properties?.reftype;
        if ( reftype == 'liquidation' ) {
            reportName = reportpath + 'ngas_liquidation_statementofrevenue.jasper'; 
        } 
        else {
            reportName = reportpath + 'ngas_remittance_statementofrevenue.jasper'; 
        }
        
        def m = svc.initReport([ objid: entity.objid, reftype: reftype ]); 
        funds = m.funds; 
        acctgroups = m.acctgroups; 

        this.fund = (funds ? funds.first() : [:]);
        this.acctgroup = (acctgroups ? acctgroups.first() : [:]);
        return preview();
    } 

    def getReportData() {
        def m = [ objid: entity.objid, reftype: reftype ];
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