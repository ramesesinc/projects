package com.rameses.gov.treasury.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class CollectorCashbookReportModel extends AsyncReportController {
    
    @Service('CashbookReportService') 
    def svc;
    
    @Script("User")
    def user;
    
    String title = 'Cashbook Report'
    String reportpath = 'com/rameses/gov/treasury/report/cashbook/'
    String reportName = reportpath + 'cashbook.jasper'
    
    def data;
    def accounts = [];
    def funds = [];
    def tag;
    
    void setup( inv ) {
        tag = inv?.properties?.tag; 
        init();
    }
    
    def initReport(){
        def resp = svc.initReport([ domain: 'TREASURY', roles: 'COLLECTOR,LIQUIDATING_OFFICER,CASHIER' ]); 
        accounts = resp.users.collect{[ objid: it.objid, name: it.name, title: it.title, fullname: it.name, description: it.description ]} 
        accounts.unique();
        funds = resp.funds; 
        entity.year = resp.year; 
        return 'default';
    }
  
    void buildReportData(entity, asyncHandler) {
        svc.generateReport( entity, asyncHandler );
    }
    
    void buildResult( o ) {
        data = o; 
    }
        
    def formControl = [
        getFormControls: {
            return [
                new FormControl( "integer", [caption:'Year', name:'entity.year', required:true, preferredSize:'100,19', captionWidth:100]), 
                new FormControl( "combo", [caption:'Month', name:'entity.month', required:true, items:'months', expression:'#{item.name}', preferredSize:'100,19', captionWidth:100]),
                new FormControl( "combo", [caption:'Account', name:'entity.account', required:true, items:'accounts', expression:'#{item.description ? item.description : item.fullname}', preferredSize:'0,19', captionWidth:100]),
                new FormControl( "combo", [caption:'Fund', name:'entity.fund', required:true, items:'funds', expression:'#{item.title}', preferredSize:'0,19', captionWidth:100]),
            ];
        }
    ] as FormPanelModel;
   
    Map getParameters() {
        return data.info;
    }
    
    List getMonths(){
        return dtSvc.getMonths();
    } 
} 