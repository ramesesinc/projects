package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.osiris2.reports.*;

class CashReceiptPrintDetailModel  {
    
    @Caller 
    def caller; 
    
    def entity;

    void print() { 
        entity.voided = ( entity.voided.toString().matches("1|true") ? 1 : 0);
        
        def op = null;
        def hname = "cashreceipt:printdetail:" + entity?.formno;
        try {
            op = Inv.lookupOpener( hname, [ reportData: entity ]);
        } catch(Throwable t){;} 
        
        if ( !op ) { 
            MsgBox.alert(hname + " not found"); 
            return; 
        }
        
        def report = getReportHandle( op.handle ); 
        if ( !report ) return; 
        
        if ( ReportUtil.isDeveloperMode() ) { 
            op = Inv.lookupOpener("report:view", [report: report]); 
            op.target = "self"; 
            
            if (caller != null && caller.metaClass.hasProperty(caller, 'binding')) {
                caller.binding.fireNavigation( op ); 
            } 
            //exit 
            return; 
        } 

        report.viewReport();                
        ReportUtil.print( report.report, true) ; 
    } 
    
    def getReportHandle( o ) {
        println 'handle -> '+ o;
        if ( o == null ) return null; 
        else if ( o instanceof ReportModel ) return o; 
        else if ( o instanceof Opener ) {
            return getReportHandle( o.handle ); 
        }
        
        try {
            println 'handle is '+ o; 
            println 'reportHandle is '+ o.reportHandler;
            return getReportHandle( o.reportHandler ); 
        } catch(Throwable t) {
            return null; 
        } 
    }
}    