package com.rameses.clfc.ledger.remarks.collector

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class CollectorRemarksController 
{
    @Service('LoanLedgerRemarksService')
    def service;
    
    def entity;
    void open() {
        entity = service.open(entity);
    }    
    
    def close() {
        return '_close';
    }
}

