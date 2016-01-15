package com.rameses.clfc.treasury.ledger.amnesty

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
    
    @Service('LedgerAmnestyService')
    def service;
    
    def entity, mode = 'read';
    
    void init() {}
    
    def borrowerLookup = Inv.lookupOpener("amnestyledger:lookup", [
        onselect: { o->
            entity.ledgerid = o.objid;
            def item = service.getLedgerInfo([ledgerid: o.objid]);
            if (item) {
                if (!item.borrower?.address) item.borrower.address = o.borrower.address;
                entity.putAll(item);
            }
            binding?.refresh('entity.ledger.*');
        }
    ]);
}

