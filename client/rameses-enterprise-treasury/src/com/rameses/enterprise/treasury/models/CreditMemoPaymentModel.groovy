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
            entry.reftype = 'CREDITMEMO';
            entry.amount = o.amount;
            entry.particulars = o.refno + " " + (!o.particulars?'':o.particulars);
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
        funds.each { f->
            f.refid = entry.refid;
            f.refno = entry.refno;
            f.refdate = entry.refdate; 
            f.reftype = entry.reftype;
            f.bank = entry.bankaccount.code + "-" + entry.bankaccount.name;
            f.particulars = f.fund.title;
            arr << f;
        }
        saveHandler( arr );
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
    
} 