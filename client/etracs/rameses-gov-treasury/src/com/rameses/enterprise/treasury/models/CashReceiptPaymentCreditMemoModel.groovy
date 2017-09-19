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
            entry.fund = o.bankaccount.fund;
            entry.particulars = "CM " + entry.refno + " " + entry.bankaccount.bank.name + " " + entry.fund.title;
            binding.refresh();
        }
        return Inv.lookupOpener("creditmemo:forreceipt:lookup", [onselect:h] );
    }
    
    void clearEntry() {
        entry = [:];
    }
    
    void addEntry() {
        def df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        def d1 = df.parse( df.format( entity.receiptdate ));
        def d2 = df.parse( df.format( entry.refdate ));
        if( !d1.equals(d2) ) {
            def str = "The receipt date is not the same as the credit memo date. Ignore and proceed ?";
            boolean t = MsgBox.confirm(str);
            if(!t) return null;
        }
        
        def f = fundList.find{ it.fund.objid == entry.fund.objid };
        if(!f) throw new Exception("Fund for payment not found");
        if(f.amount != entry.amount )
            throw new Exception("Amount for " + f.fund.title + " must be " + f.amount );
        fundList.remove(f);
        payments << entry;
    }
    
    boolean getHasBalance() {
        return (payments.sum{it.amount} != entity.amount);
    }
    
    def savePayment() {
        if( payments.sum{it.amount} != entity.amount )
            throw new Exception("Amount must equal to the total credit memo amount");
        
        def m = [:];
        m.paymentitems = payments;
        saveHandler(m);  
        return "_close";
    }
    
    
} 