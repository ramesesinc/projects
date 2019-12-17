package com.rameses.gov.etracs.ngas.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
        
class JevLiquidationReportModel extends com.rameses.osiris2.reports.ReportController {

    @Binding
    def binding;

    @Service('JevLiquidationReportService')
    def svc;

    def title = 'Journal Entry Voucher Report';

    String reportPath = "com/rameses/gov/etracs/ngas/report/";
    String reportName;
    
    def data = [:]; 
    def funds; 
    def fund; 
    def acctgroups; 
    def acctgroup; 

    def initReport() {
        reportName = reportPath + 'ngas_liquidation_jev.jasper'; 
        
        def m = svc.initReport([ objid: entity.objid ]); 
        funds = m.funds; 
        acctgroups = m.acctgroups; 

        this.fund = (funds ? funds.first() : [:]);
        this.acctgroup = (acctgroups ? acctgroups.first() : [:]);
        return preview();
    } 
    
    SubReport[] getSubReports() { 
        return [ 
            new SubReport("ITEMS", reportPath + "ngas_liquidation_jevitems.jasper"),
            new SubReport("SHARES", reportPath + "ngas_liquidation_jevshares.jasper"),
        ] as SubReport[]; 
    }

    def getReportData() {
        def m = [ objid: entity.objid ];
        m.fund = this.fund; 
        m.acctgroup = this.acctgroup; 
        
        def res = svc.getReport( m ); 
        data.clear(); 
        data.putAll( res ); 
        return data;
    }

    Map getParameters(){
        return data?.header; 
    }
}