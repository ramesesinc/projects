package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.common.* 

class CashReceiptPaymentCreditMemoModel extends PageFlowController { 

    @Binding
    def binding;
    
    def entity;
    def entry;
    def fundList;
    def saveHandler;
    def payments = [];
    
    def noncashListHandler = [
        fetchList: { return payments; }, 
    ] as BasicListModel;
    
    def getLookupCreditMemo() {
        def h = {o->
            entry = [:];
            entry.refid = o.objid;
            entry.refno = o.refno;
            entry.refdate = o.refdate;
            entry.payer = o.payer;
            entry.bankaccount = o.bankaccount;
            entry.bankaccountid = o.bankaccount.objid;
            entry.reftype = 'CREDITMEMO';
            entry.amount = o.amount;
            binding.refresh();
        }
        return Inv.lookupOpener("creditmemo:forreceipt:lookup", [onselect:h] );
    }
    
    void distributeFund() {
        if( entry.amount != entity.amount )
            throw new Exception("Amount must equal to the credit memo amount");
            
        def df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        def d1 = df.parse( df.format( entity.receiptdate ));
        def d2 = df.parse( df.format( entry.refdate ));
        if( !d1.equals(d2) ) {
            def str = "The receipt date is not the same as the credit memo date. Ignore and proceed ?";
            boolean t = MsgBox.confirm(str);
            if(!t) return null;
        }
        payments = [];
        int i = 1;
        int cnt = fundList.size();
        fundList.each { f->
            def m = [:];
            m.reftype =  "CREDITMEMO"; 
            m.refno = entry.refno;
            m.refid = entry.refid;
            m.amount = f.amount;
            m.particulars = "CM " + entry.refno + " " + entry.bankaccount.bank.name + ((cnt>1)? " " + (i++) + " of " +cnt : "" );
            m.refdate = entry.refdate; 
            m.fund = f.fund;
            payments << m;
        }
    }
    
    def savePayment() {
        def m = [:];
        m.creditmemo = entry;
        m.paymentitems = payments;
        saveHandler(m);  
        return "_close";
    }
    
    
} 