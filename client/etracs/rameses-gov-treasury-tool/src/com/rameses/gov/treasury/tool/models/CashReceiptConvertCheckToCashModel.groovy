package com.rameses.gov.treasury.tool.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.BreakException;

class CashReceiptConvertCheckToCashModel {

    @Invoker
    def inv; 
    
    @Script('User')
    def user;
    
    @Service('QueryService') 
    def querySvc;
        
    @Service('CashReceiptModifyService') 
    def modifySvc;
    
    @Binding 
    def binding;
    
    def receipt; 
    def remarks;
    def mode; 
    
    void init() {
        mode = 'init'; 
        receipt = null; 
        remarks = null;
    }
    
    def getTitle() {
        def buff = new StringBuilder(); 
        buff.append("Convert Check To Cash"); 
        if ( mode == 'init') buff.append(": Initial"); 
        else if ( mode == 'finish') buff.append(": Finished"); 
        return buff.toString(); 
    }
    
    def getLookupReceipt() {
        def param = ['query.userid' : user.profile.USERID]; 
        param.onselect = { o-> 
            if ( o.totalnoncash == 0 ) { 
                throw new Exception('Receipt must be paid in check. Please verify.'); 
            }
            receipt = o; 
        }
        param.onempty = { 
            receipt = null; 
        }
        return Inv.lookupOpener('unremitted_cashreceipt:lookup', param);  
    } 
    
    def decformat = new java.text.DecimalFormat("#,##0.00");  
    def getReceiptAmount() {
        def amount = (receipt?.amount ? receipt.amount : 0.0); 
        return decformat.format( amount ); 
    }
    def getReceiptNonCash() {
        def totalnoncash = (receipt?.totalnoncash ? receipt.totalnoncash : 0.0); 
        return decformat.format( totalnoncash ); 
    }  
    def getReceiptCash() {
        def amount = (receipt?.amount ? receipt.amount : 0.0); 
        def totalnoncash = (receipt?.totalnoncash ? receipt.totalnoncash : 0.0); 
        return decformat.format( amount - totalnoncash ); 
    } 
    
    def doCancel() {
        if ( mode == 'finish') return '_close'; 
        
        if ( MsgBox.confirm('You are about to close this window. Continue?')) {
            return '_close'; 
        }
        return null; 
    }

    def doPost() {
        if ( !receipt ) throw new Exception('Please specify a receipt'); 
        if ( !remarks ) throw new Exception('Please specify a remarks'); 
        if ( !MsgBox.confirm('You are about to post this transaction. Continue?')) return null; 
        
        receipt.remarks = remarks;  
        modifySvc.convertCheckToCash( receipt ); 
        mode = 'finish'; 
        return mode; 
    }
    
    def doNew() {
        init(); 
        return 'default'; 
    }
}