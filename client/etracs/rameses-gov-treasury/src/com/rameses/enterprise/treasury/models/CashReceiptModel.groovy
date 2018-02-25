package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.reports.*;
import com.rameses.util.*;

class CashReceiptModel extends CrudFormModel { 

    String schemaName = "cashreceipt";    
    
    def report;     
    
    
    def reprint() {
        if ( !verifyReprint() ) { 
            MsgBox.alert('Invalid security code'); 
            return; 
        }

        //check fist if form handler exists.
        def o = InvokerUtil.lookupOpener( "cashreceipt-form:"+entity.formno, [ reportData: entity ] );
        if ( !o ) throw new Exception("Handler not found");
        
        if ( entity.receiptdate instanceof String ) { 
            // this is only true when txnmode is OFFLINE 
            try {
                def dateobj = YMD.parse( entity.receiptdate ); 
                entity.receiptdate = dateobj; 
            } catch( Throwable t ) {;} 
        } 

        def handle = o.handle;
        def opt = handle.viewReport(); 
        if ( opt instanceof Opener ) { 
            // 
            // possible routing of report opener has been configured 
            // 
            handle = opt.handle; 
            handle.viewReport(); 
        } 
        
        if ( handle instanceof com.rameses.osiris2.reports.ReportModel ) {
            report = handle; 
        } else { 
            report = handle.report; 
        } 
        
        if ( ReportUtil.isDeveloperMode() ) {
            return 'preview'; 
        } 
        
        if ( report instanceof com.rameses.osiris2.reports.ReportModel ) { 
            report.print(); 
        } else { 
            ReportUtil.print(report, true); 
        } 
        return null; 
    }

    boolean verifyReprint() {
        return (MsgBox.prompt("Please enter security code") == "etracs"); 
    }
    
    boolean isAllowVoid() { 
        if ( entity.voided==true || entity.voided==1 ) return false; 
        if ( entity.remitted==true || entity.remitted==1 ) return false; 
        
        return true; 
    } 
    
    def showReport = { o->   
        if ( o instanceof com.rameses.osiris2.reports.ReportModel ) {
            report = o; 
            report.viewReport(); 
            binding.fireNavigation('preview');  
        } 
    } 
    
} 