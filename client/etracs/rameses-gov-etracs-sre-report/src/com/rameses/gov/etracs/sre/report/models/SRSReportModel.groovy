package com.rameses.gov.etracs.sre.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class SRSReportModel extends AsyncReportController {

    @Service('SRSReportService') 
    def svc;
    
    @Service('OrgService')
    def orgSvc;

    String title = 'Statement of Receipts Sources'
    String reportpath = 'com/rameses/gov/etracs/sre/report/'
    String reportName = reportpath + 'statement_of_receipts_sources.jasper'
    
    def funds;
    def months;
    def acctgroups;
    def accttypes;
    def data;

    def periods = [
        [type:'quarterly', title:'Quarterly'],
        [type:'monthly', title:'Monthly'],
        [type:'daily', title:'Daily'],
        [type:'asofqtr', title:'As of Quarter'],
        [type:'asofmonth', title:'As of Month'], 
        [type:'asofday', title:'As of Day'] 
    ];
    
    def postingTypes = [
        [objid: 'BY_REMITTANCE_DATE',  name: 'By Remittance Date'],
        [objid: 'BY_LIQUIDATION_DATE', name: 'By Liquidation Date']
    ]; 
    
    def detailtypes = [true, false];
    def reportTypes = ['standard', 'extended','itemaccount'];
    
    def initReport(){
        entity.type = 'standard'; 
        entity.optwithdetail = false; 
        entity.opthidenoactual = true;
        entity.period = periods[0]; 
        entity.withdetail = false; 
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
        
        return [
            TITLE:data.title, 
            PERIOD:data.period, 
            TOTALACTUAL: data.totalactual, 
            TOTALTARGET: data.totaltarget, 
            TOTALEXCESS: data.totalexcess, 
            TOTALPERCENTAGE: data.totalpercentage,
            TOTALPERCENTAGEVALUE: data.totalpercentagevalue, 
            HIDEINCOMETARGET: (entity.opthideincometarget==true), 
            COLLECTOR_ID  : entity.collector?.objid, 
            COLLECTOR_NAME: entity.collector?.name, 
            ACCTGROUP  : entity.acctgroup?.title, 
            ACCTTYPE   : entity.accttype?.title, 
            FUND_ID    : entity.fund?.objid, 
            FUND_TITLE : fundtitle 
        ]; 
    } 

    def lookupCollector = Inv.lookupOpener('collector:lookup', [
        onselect: { o-> 
            def name = [o.firstname, o.middlename, o.lastname].findAll{(it? true: false)}.join(' '); 
            entity.collector = [objid: o.objid, name: name]; 
        }, 
        onempty: { 
            entity.collector = null; 
        } 
    ]); 
}