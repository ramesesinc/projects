package com.rameses.gov.treasury.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class CashbookLiquidationReportModel extends ReportController {

    @Service('CashbookLiquidationReportService') 
    def svc; 
    
    @Script('ReportPeriod') 
    def reportPeriod;
    
    String title = 'Cashbook Liquidation Report';
    String reportpath = 'com/rameses/gov/treasury/report/';
    String reportName = reportpath + 'cashbook_liquidation.jasper';

    def data;
    def tag;

    def periods = [
        [code:'daily', name:'DAILY'],
        [code:'monthly', name:'MONTHLY'],
    ]; 
    
    def doInit( inv ) { 
        tag = inv?.properties?.tag; 

        try { 
            return init(); 
        } finally { 
            entity.period = periods[0]; 
            
            def strdate = new java.sql.Date( System.currentTimeMillis()).toString(); 
            entity.year = strdate.split('-')[0]; 
            entity.date = strdate; 
            if ( tag == null ) { 
                def ctx = com.rameses.rcp.framework.ClientContext.currentContext; 
                entity.account = [ 
                    objid : ctx.headers.USERID, 
                    name  : ctx.headers.FULLNAME 
                ]; 
            }
        }
    }
       
    def getReportData(){
        data = svc.getReport(entity);
        if ( !data?.items ) throw new Exception('No report data found'); 
        
        return data.items; 
    }
       
    Map getParameters() { 
        return data.header; 
    } 
}