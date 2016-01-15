package com.rameses.clfc.patch.loan.cancel

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class CancelLoanController 
{
    @Binding
    def binding;
    
    @Service('CancelLoanService')
    def service;
    
    String title = 'Cancel Loan';
    
    def action = 'init', entity;
    def loanappLookup = Inv.lookupOpener('cancel-loanapp:lookup', [
         onselect: { o->
             entity = o;
             binding?.refresh();
         }
    ]);
    
    void init() {
        action = 'init';
        entity = [:];
    }
    
    def next() {
        entity = service.openLoan(entity);
        action = 'main';
        return 'main';
    }
    
    def close() {
        return '_close';
    }
    
    def back() {
        action = 'init';
        return 'default';
    }
    
    def cancelLoan() {
        if (!MsgBox.confirm('You are about to cancel this loan. Continue?')) return;
        
        service.cancelLoan(entity);
        MsgBox.alert("Successfully cancelled loan.");
        init();
        return 'default';
    }
}

