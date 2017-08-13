package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.common.* 

class CreditMemoPaymentModel  { 

    @Binding
    def binding;
    
    def entity;
    def entry;
    
    def funds;
    def saveHandler;
    
    void init() {
        entry = [:]; 
        if(!funds) throw new Exception("fundbreakdown is required");
    }
    
    def fundList = [
        fetchList: { o->
            return funds;
        }
    ] as BasicListModel;
    
    def getLookupCreditMemo() {
        def h = {o->
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
    
    def doOk() {
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
        def arr = [];
        int i = 1;
        int cnt = funds.size();
        funds.each { f->
            f.refid = entry.refid;
            f.refno = entry.refno;
            f.refdate = entry.refdate; 
            f.reftype = entry.reftype;
            f.bank = entry.bankaccount.bank.name;
            f.bankid = entry.bankaccount.bank.objid;
            f.bankaccountid = entry.bankaccountid;
            f.particulars = "CM " + f.refno + " " + f.fund.title + " " + entry.bankaccount.bank.name + ((cnt>1)? " " + (i++) + " of " +cnt : "" );
            arr << f;
        }
        saveHandler( arr );
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
    
} 