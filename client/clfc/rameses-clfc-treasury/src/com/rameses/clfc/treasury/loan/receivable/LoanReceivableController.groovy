package com.rameses.clfc.treasury.loan.receivable

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class LoanReceivableController 
{
    @Binding
    def binding;
    
    @Service("LoanReceivableService")
    def service;
    
    def entity, mode = 'read';
    
    void open() {
        entity = service.open([objid: entity.objid]);
        mode = 'read';
    }
    
    def close() {
        return "_close";
    }
}

