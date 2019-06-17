package com.rameses.gov.treasury.tool.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.BreakException;

class DishonorCheckPaymentModel {

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
        buff.append("Convert Cash To Check"); 
        if ( mode == 'init') buff.append(": Initial"); 
        else if ( mode == 'finish') buff.append(": Finished"); 
        return buff.toString(); 
    }
    
    def getLookupReceipt() {
        def param = ['query.userid' : user.profile.USERID]; 
        param.onselect = { o-> 
            if ( o.totalnoncash > 0 ) { 
                throw new Exception('Receipt must be paid in cash. Please verify.'); 
            }
            receipt = o; 
        }
        param.onempty = { 
            receipt = null; 
        }
        return Inv.lookupOpener('unremitted_cashreceipt:lookup', param);  
    } 

    def getLookupCheck() {
        if ( !receipt?.objid ) { 
            MsgBox.alert('Please specify a receipt first'); 
            throw new BreakException(); 
        }
        
        def param = ['query.userid' : user.profile.USERID];  
        return Inv.lookupOpener('unuse_checkpayment:lookup', param);  
    } 
    
    def decformat = new java.text.DecimalFormat("#,##0.00");  
    def getReceiptAmount() {
        def amount = (receipt?.amount ? receipt.amount : 0.0); 
        return decformat.format( amount ); 
    }
   
    def getCheckAmount() {
        def amount = check?.amount; 
        if ( amount == null ) return "";
        return decformat.format( amount ); 
    }
    
    def getCheck() {
        if ( receipt?.updateinfo?.checks ) { 
            return receipt.updateinfo.checks.first(); 
        }
        else if (receipt?.updateinfo?.paymentitems ) {
            def payitem = receipt.updateinfo.paymentitems.first();
            def chk = [:]; 
            chk.putAll( payitem.check ); 
            chk.amount = receipt.amount; 
            return chk; 
        }
        return null; 
    }
    void createCheckPayment() { 
        if ( !receipt?.objid ) throw new Exception('Please specify a receipt first'); 
        
        def rct = findReceipt(); 
        if ( !rct ) throw new Exception('Cash Receipt transaction not found');
        
        def param = [ entity: rct, fundList: []]; 
        rct.items.groupBy{ it.item.fund }.each{ k,v->
            param.fundList << [fund:k, amount: v.sum{it.amount}];
        }
        if ( !param.fundList ) throw new Exception("Please provide a fund for each item"); 
        
        param.options = [ allowSplit: false ]; 
        param.saveHandler = { o-> 
            receipt.updateinfo = o; 
            binding.notifyDepends("check"); 
        }
        Modal.show( "cashreceipt:payment-check", param );         
    }
    
    def findReceipt() {
        def receiptid = receipt?.objid; 
        if ( !receiptid ) return null; 
        
        def rct = querySvc.findFirst([_schemaname:'cashreceipt', findBy: [objid: receiptid]]); 
        if ( !rct?.objid ) return null;
        
        rct.items = querySvc.getList([_schemaname:'cashreceiptitem', findBy: [receiptid: receiptid]]); 
        return rct; 
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
        if ( !check ) throw new Exception('Please specify a receipt'); 
        if ( !remarks ) throw new Exception('Please specify a reason'); 
        if ( !MsgBox.confirm('You are about to post this transaction. Continue?')) return null; 
        
        receipt.remarks = remarks; 
        modifySvc.convertCashToCheck( receipt ); 
        mode = 'finish'; 
        return mode; 
    }
    
    def doNew() {
        init(); 
        return 'default'; 
    }
}