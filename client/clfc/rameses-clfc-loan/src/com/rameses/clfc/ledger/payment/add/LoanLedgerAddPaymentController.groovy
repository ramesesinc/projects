package com.rameses.clfc.ledger.payment.add;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class LoanLedgerAddPaymentController
{
    @Binding
    def binding;

    @Service('LoanLedgerService')
    def svc;
    
    String title = "Ledger Add Payment";

    def entity;
    def mode = 'read';
    def data;

    void init() {
        println 'entity-> '+entity;
        mode = 'create';
        data = [:];
    }

    def close() {
        return "_close";
    }

    def create() {
        init();
        binding.refresh();
    }

    def save() {
        def m = [:];
        data = svc.addPayment(m);
        mode = 'read';
        binding.refresh();
    }
}