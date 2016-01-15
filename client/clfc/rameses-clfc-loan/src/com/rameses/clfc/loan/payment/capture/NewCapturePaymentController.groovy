package com.rameses.clfc.loan.payment.capture;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class NewCapturePaymentController
{
    @Binding
    def binding;

    @Service("LoanCaptureNewPaymentService")
    def service;

    @Service("BankService")
    def bankSvc;
    
    @PropertyChangeListener
    def listener = [
        'option': { o->
            payment.option = o;
            payment.check = [:];
            payment.bank = [:];
        }
    ]

    def entity;
    def payment;
    def bankList;
    def optionsList = ["cash", "check"];
    def option;
    def mode;
    def borrower;
    /*
    def borrowerLookup = Inv.lookupOpener('capturepaymentborrower:lookup', [
        remittanceid: remittanceid, 
        onselect: { o->
            entity = o;
            //println 'entity ' + entity;
            binding.refresh();
        }
    ]);
    */

    def remittanceid;
    def refreshHandler;

    void init() {
        entity = [:];
        mode = 'create';
        payment = [objid: 'PYMT' + new UID()];
        bankList = bankSvc.getList([state: 'ACTIVE']);
        option = [:];
    }
    
    def getBorrowerLookup() {
        def params = [
            remittanceid: remittanceid,
            onselect    : { o->
                entity = o;
                binding?.refresh();
            }
        ];
        def op = Inv.lookupOpener('capturepaymentborrower:lookup', params);
        if (!op) return null;
        return op;
    }
    
    /*
    public void setOption( option ) {
        this.option = option;
        payment.option = option;
        payment.check = [:];
        payment.bank = [:];
    }
    */

    void save() {
        def msg = "<html>You are about to add payment for borrower <b>" + entity.borrower.name + "</b>. Continue?</html>";
        if (!MsgBox.confirm(msg)) return;
        
        entity.remittanceid = remittanceid;
        entity.payment = payment;
        entity = service.create(entity);
        mode = 'read';
        if (refreshHandler) refreshHandler();
    }

    def cancel() {
        if (refreshHandler) refreshHandler();
        return "_close";
    }
    
    def close() {
        return '_close';
    }

    void create() {
        init();
    }
}