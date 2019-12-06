package com.rameses.gov.etracs.ngas.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class StatementOfRevenueReportModel extends AsyncReportController {

    @Service('NGASStatementOfRevenueReportSummaryService') 
    def svc;
    
    @Service('OrgService')
    def orgSvc;

    String title = 'Statement of Revenue'
    String reportpath = 'com/rameses/gov/etracs/ngas/report/'
    String reportName = reportpath + 'ngas_statementofrevenue_summary.jasper'
    
    def funds;
    def months;
    def acctgroups;
    def accttypes;
    def data;

    def periods = [
        [type:'monthly', title:'Monthly'],
        [type:'quarterly', title:'Quarterly'],
        [type:'daily', title:'Daily'],
        [type:'asofmonth', title:'As of Month'], 
        [type:'asofqtr', title:'As of Quarter'],
        [type:'asofday', title:'As of Day'] 
    ];
    
    def postingTypes = [
        [objid: 'BY_REMITTANCE_DATE',  name: 'By Remittance Date'],
        [objid: 'BY_LIQUIDATION_DATE', name: 'By Liquidation Date']
    ]; 
    
    def reportTypes = ['standard', 'extended','itemaccount'];
    
    def initReport(){
        entity.type = 'standard'; 
        entity.period = periods[1];
        def res = svc.initReport(); 
        months = res.months; 
        accttypes = res.accttypes;
        acctgroups = res.acctgroups; 
        
        funds = []; 
        def groups = res.funds.collect{[ objid: it.groupid, indexno: it.groupindexno ]}.unique();  
        groups.sort{ it.indexno }.each{ g-> 
            funds << g;
            g.category = 'group'; 
            g.desc = g.objid + ' GROUP'; 
            res.funds.findAll{( it.groupid == g.objid )}.each{ f-> 
                funds << f; 
                f.category = 'item'; 
                f.desc = '    '+ f.title;
            }
        }
        return 'default'; 
    }
    
    void buildReportData(entity, asyncHandler) { 
        def var = orgSvc.getRoot();
        entity.lgu = var.name;
        svc.getReport(entity, asyncHandler);
    } 

    Map getParameters() {
        def fundtitle = entity.fund?.title; 
        if ( entity.fund?.category == 'group') {
            fundtitle = funds.findAll{( it.groupid == entity.fund.objid )}.collect{ it.title }.join(", "); 
        }
        
        def map = [:]; 
        if ( data?.header ) {
            map.putAll( data.header ); 
        }
        map.ACCTGROUP = entity.acctgroup?.title;
        map.ACCTTYPE = entity.accttype?.title;
        return map;
    } 
}