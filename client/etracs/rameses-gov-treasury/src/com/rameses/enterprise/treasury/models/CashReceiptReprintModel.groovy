package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.seti2.models.*;

class CashReceiptReprintModel  {
    
    @Service("CashReceiptReprintService")
    def service;

    @Invoker
    def invoker;

    @Caller
    def caller;

    @Script("User")
    def user;
    
    @Binding 
    def binding;
    
    def report;  
    
    def username;
    def password;
    def remarks;
    
    def entity;
    def receipt;
    boolean applySecurity = false;
    
    void reprintUnsecured() {
        applySecurity = false;
        receipt = caller.entity;
    }

    void reprintSecured() {
        applySecurity = true;
        receipt = caller.entity;
    }
    
    void doOk() {
        if( applySecurity ) {
            entity.username = username;
            entity.password = user.encodePwd( password, username );
        }
        entity.applysecurity = applySecurity;
        entity.receiptid = receipt.objid;
        entity.reason = remarks;
        service.verifyReprint( entity );
        
        def op = reprint(); 
        if ( op instanceof Opener ) { 
            binding.fireNavigation('_close'); 
            if (caller.metaClass.hasProperty(caller, 'binding')) {
                caller.binding.fireNavigation( op ); 
            } 
        } else {
            binding.fireNavigation('_close'); 
        }
    } 

    def doCancel() {
        return "_close";
    }

    def findReportOpener( reportData ) { 
        //check first if form handler exists. 
        def o = Inv.lookupOpener( "cashreceipt-form:"+entity.formno, [reportData:reportData] );
        if ( !o ) throw new Exception("Handler not found"); 

        if ( reportData.receiptdate instanceof String ) { 
            // this is only true when txnmode is OFFLINE 
            try {
                def dateobj = YMD.parse( reportData.receiptdate ); 
                reportData.receiptdate = dateobj;  
            } catch( Throwable t ) {;} 
        } 
        return o.handle; 
    } 
    

    def reprint() {
        def handle = findReportOpener(entity);
        def opt = handle.viewReport(); 
        if ( opt instanceof Opener ) { 
            handle = opt.handle; 
            handle.viewReport(); 
        } 
        
        def report = null; 
        if ( handle instanceof com.rameses.osiris2.reports.ReportModel ) {
            report = handle; 
        } else { 
            report = handle.report; 
        } 
        
        if ( ReportUtil.isDeveloperMode() ) {
            def op = Inv.lookupOpener("report:view", [report: report]); 
            op.target = "self"; 
            return op; 
        }         
        
        def canShowPrinterDialog = ( entity._options?.canShowPrinterDialog == false ? false : true ); 
        ReportUtil.print(report, canShowPrinterDialog);
        return null; 
    }

    
}    