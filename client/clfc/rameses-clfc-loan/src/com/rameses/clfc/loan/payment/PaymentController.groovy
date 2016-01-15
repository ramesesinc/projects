package com.rameses.clfc.loan.payment;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.framework.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class PaymentController
{
    @Binding
    def binding;

    @Service("DateService")
    def dateSvc;
    
    @Service("BankService")
    def bankSvc;
    
    @Service('LoanOnlineCollectionService')
    def service;
    
    def entity;
    def bill;
    def optionList = [], bankList = [];
    def mode = 'init';
    
    String title = "Collect Direct Payment";
    
    @PropertyChangeListener
    def listener = [
        "entity.payoption": { o->
            if (o == 'cash') {
                entity.bank = null;
                entity.check = [:];
                binding.refresh('entity.(bank|check.*)');
            }
        }
    ];

    def lookupBorrowerHandler = InvokerUtil.lookupOpener('ledgerborrower:lookup', [
        onselect: {o->
            entity.borrower = o.borrower;
            entity.route = o.route;
            entity.loanapp = o.loanapp;
            binding.refresh('entity');
        }
    ])

    void init() {
        mode = 'init';
        entity = [
            objid       : 'OCD' + new UID(),
            parentid    : 'OC' + new UID(),
            txndate     : dateSvc.getServerDateAsString().split(" ")[0],
            payoption   : 'cash',
            collector   : [
                objid: ClientContext.currentContext.headers.USERID,
                name: ClientContext.currentContext.headers.NAME
            ],
            bank        : [:],
            check       : [:]
        ];
        optionList = service.getPaymentOptions();
        bankList = bankSvc.getList([state: 'ACTIVE']);
    }

    def next() {
        mode = 'collect'
        bill = service.computeBilling(entity);
        entity.refno = bill.refno;
        entity.paytype = bill.paymentmethod;
        return 'main';
    }

    def back() {
        init();
        binding.refresh('entity');
        return 'default';
    }

    def save() {
        if (MsgBox.confirm("The amount paid is "+entity.amount+". Continue?")) {
            service.create(entity);
            MsgBox.alert("Payment successfully saved!");
            init();
            return 'default';
        }
    }
    
    def getOptionList() {
        if (!service) return [];
        return service.getPaymentOptions();
    }

    def close() {
        return '_close';
    }
}