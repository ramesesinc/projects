package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class FundTransferModel extends CrudFormModel {
    
    def fromfundid;
    def tofundid;
    
    void afterCreate() {
        
    }
    
    def getFromBankAccountLookup() {
        def h = { o->
            entity.frombankaccount = o;
        }
        return  Inv.lookupOpener( "bankaccount:lookup", [fundid: fundid, onselect: h ]);
    }
    
    def getToBankAccountLookup() {
        def h = { o->
            entity.tobankaccount = o;
        }
        return  Inv.lookupOpener( "bankaccount:lookup", [fundid: fundid, onselect: h ]);
    }
    
    def doCancel() {
        return "_close";
    }

    def doOk() {
        if(entry.prefix==null) entry.prefix = '';
        if(entry.suffix==null) entry.suffix = '';
        service.addBatch( entry );
        handler( entry );
        return "_close";
    }
    
}    