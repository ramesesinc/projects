package com.rameses.clfc.treasury.loan.ar

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class LoanARItemController 
{
    @Binding
    def binding;
    
    def entity, handler, mode = 'read';
    def loanapp;
    
    void init() {
        entity = [objid: 'LARD' + new UID()];
    }
    
    def loanappLookup = Inv.lookupOpener('loanapp:lookup', [
         onselect: { o->
             if (o.state == 'CLOSED') 
                throw new Exception("This Loan Application is already closed.");
             
             entity.loanapp = [objid: o.objid, appno: o.appno];
             entity.borrower = o.borrower;
         }
    ]);
    
    def doOk() {
        if (handler) handler(entity);
        return '_close';
    }
    
    def doCancel() {
        return '_close';
    }
}

