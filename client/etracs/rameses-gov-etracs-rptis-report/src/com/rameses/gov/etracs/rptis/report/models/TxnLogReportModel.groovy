package com.rameses.gov.etracs.rptis.report.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class TxnLogReportModel extends AsyncReportModel2
{
    @Service('RPTReportTxnLogService') 
    def svc 
    
    def users;
    def reftypes;
    String title = 'Transaction Log Listing';
    
    String reportName = 'com/rameses/gov/etracs/rptis/reports/list_txn_log.jasper';
    
        
    def initReport(){
        entity.qtr = null;
        if (!users) {
            users = svc.getUsers();
            reftypes = svc.getRefTypes();
        }
        return 'default';
    }
            
    void buildReportData(entity, asyncHandler){
        svc.buildReport(entity, asyncHandler)
    }
    
    Map getParameters(){
        return data.parameters;
    }
    
   def formControl = [
        getFormControls: {
            return [
                new FormControl( "integer", [captionWidth:110, caption:'Year', name:'entity.year', required:true, preferredSize:'100,19']),
                new FormControl( "combo", [captionWidth:110, caption:'Month', name:'entity.month', items:'months', depends:'entity.year', expression:'#{item.caption}', required:true, dynamic:true, preferredSize:'100,19']),
                new FormControl( "combo", [captionWidth:110, caption:'Day', name:'entity.day', items:'days', depends:'entity.year,entity.month', dynamic:true, preferredSize:'100,19', emptyText:'ALL']),
                new FormControl( "combo", [captionWidth:110, caption:'User', name:'entity.user', items:'users', expression:'#{item.name}', emptyText:'ALL']),
                new FormControl( "combo", [captionWidth:110, caption:'Reference Type', name:'entity.reftype', items:'reftypes', emptyText:'ALL']),
            ]
        },
   ] as FormPanelModel;
   
}