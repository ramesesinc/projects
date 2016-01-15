package com.rameses.clfc.loan.capture;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;
import com.rameses.clfc.util.*;

class LoanAppCaptureLedgerController
{
    @Binding
    def binding;
    
    def service;

    def paymentTypes = LoanUtil.paymentTypes;    
    def application;
    def entity;
    def mode = 'read';
    def title = 'Capture Ledger';
    
    LoanAppCaptureLedgerController() {
        try {
            service = InvokerProxy.instance.create("LoanLedgerService");
        } catch (ConnectException ce) {
            ce.printStackTrace();
            throw ce;
        }
    }
    
    @PropertyChangeListener
    def listener = [
        "entity.paymentmethod": { o->
            if (o == 'schedule') {
                entity.overpayment = 0;
                binding?.refresh('entity.overpayment');
            }
        }
    ]
    
    void init() {
        mode = 'read';
        entity = [ 
            objid           : 'LEDGER'+new UID(),
            txnmode         : 'CAPTURE',
            payments        : [],
            overpayment     : 0
        ]
    }
    
    def close() {
        return '_close';
    }
    
    def next() {
        mode = 'create';
        entity = service.resolveEntity(entity);
        if (!entity.payments) entity.payments = [];
        entity.payments.clear();
        entity.paymentmethod = null;
        paymentsHandler.reload();
        return 'main';
    }
    
    def back() {
        mode = 'read';
        return 'default';
    }
    
    def save() {
        if (paymentsHandler.hasUncommittedData())
            throw new Exception('Please commit table data before saving.');
        
        entity.acctid = entity.borrower.objid;
        entity.acctname = entity.borrower.name;
        entity = service.create(entity);
        mode = 'read';
        MsgBox.alert('Transaction has been successfully saved'); 
        init();
        return 'default';
    }
    
    def cancel() {
        if(MsgBox.confirm("You are about to close this window. Continue?")) {
            return '_close';
        }
    }
    
    def getAppLookupHandler() {
        def handler = {o->
            entity.appno = o.appno;
            entity.loanamount = o.loanamount;
            entity.appid = o.objid;
            entity.borrower = o.borrower;
            entity.producttypeid = o.producttype?.name;
            entity.interestrate = o.producttype?.interestrate;
            entity.overduerate = o.producttype?.overduerate;
            entity.underpaymentrate = o.producttype?.underpaymentpenalty;
            entity.absentrate = o.producttype?.absentpenalty;
            entity.term = o.producttype?.term;
            entity.route = o.route;
            //entity.dtstarted = o.dtstarted;
            entity.dtreleased = o.dtreleased;
            //entity.dtmatured = o.dtmatured;
        }
        return InvokerUtil.lookupOpener("capture_loanapp:lookup", [onselect:handler]);
    }
    
    def paymentsHandler = [
        fetchList: {o->
            if(!entity.payments) entity.payments = [];
            return entity.payments;
        },
        createItem: {
            return [objid: 'PYMNT'+new UID()]
        },
        onAddItem: {o->
            entity.payments.add(o);
        },
        onRemoveItem: {o->
            if(MsgBox.confirm("You are about to remove this payment. Continue?")) {
                entity.payments.remove(o);
                return true;
            }
            return false;
        }
    ] as EditorListModel;
}
