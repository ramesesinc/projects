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
        MsgBox.alert("Print");
        def op = null;
        def hname = "cashreceipt:printdetail:"+entity.formno;
        try {
            op = Inv.lookupOpener( hname, [reportData:entity ] );
        }
        catch(e){;}
        if(!op) throw new Exception(hname + " not found");
        def report = op.handle;
        
        if ( ReportUtil.isDeveloperMode() ) { 
            boolean has_callback_method = false;  
            try { 
                if ( caller.showReport ) has_callback_method = true; 
            } catch(Throwable t){;} 
            
            if ( has_callback_method ) { 
                caller.showReport( report ); 
                return; 
            } 
        } 

        report.viewReport();                
        ReportUtil.print( report.report, true) ; 
    } 
    
}    