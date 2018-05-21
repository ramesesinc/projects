package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.seti2.models.*;

class CashReceiptReprintModel extends CrudFormModel {
    
    @Service("CashReceiptReprintService")
    def service;

    @Script("User")
    def user;
    
    def report;  
    
    def username;
    def password;
    def remarks;
    
    def entity;
    def receipt;
    
    void afterCreate() {
        receipt = caller.entity;
    }
    
    def doOk() {
        entity.username = username;
        entity.password = user.encodePwd( password, username );
        entity.receiptid = receipt.objid;
        entity.reason = remarks;
        service.verifyReprint( entity );
        reprint();
        return "_close";
    } 

    def doCancel() {
        return "_close";
    }

    def reprint() {
        if ( entity._options ) { 
            entity._options.canShowPrinterDialog = true; 
        }
        
        
        //check fist if form handler exists.
        def o = Inv.lookupOpener( "cashreceipt-form:"+entity.formno, [ reportData: entity ] );
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
    
    def showReport = { o->   
        if ( o instanceof com.rameses.osiris2.reports.ReportModel ) {
            report = o; 
            report.viewReport(); 
            binding.fireNavigation('preview');  
        } 
    } 
    
}    