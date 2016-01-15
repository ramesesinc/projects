package com.rameses.clfc.treasury.amnesty

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class GeneralInformationController 
{    
    @Caller
    def caller;
    
    @Binding
    def binding;
    
    def entity, mode = 'read';
    def ledger;
    
    def borrowerLookup = Inv.lookupOpener("amnestyledger:lookup", [
        onselect: { o->
            entity.borrower = o.borrower;
            entity.loanapp = o.loanapp;
            entity.ledgerid = o.objid;
            entity.ledger = [
                loanamount  : o.loanapp.loanamount,
                dtreleased  : o.dtreleased,
                dtmatured   : o.dtmatured,
                balance     : o.balance
            ];
            //entity.ledger = ledger;
            binding.refresh('entity.ledger.*');
        }
    ]);
}

