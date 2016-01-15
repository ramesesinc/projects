package com.rameses.clfc.treasury.loan.receivable

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class LoanReceivableVoidRequestController 
{
    @Service("LoanReceivableVoidRequestService")
    def service;
    
    def entity;
    
    void init() {
        entity = [objid: "LRV" + new UID()]
    }
    
    void open() {
        entity = service.open([objid: entity.objid]);
    }
    
    void approveDocument() {
        if (!MsgBox.confirm("You are about to approve this document. Continue?")) return;
        
        entity = service.approveDocument(entity);
    }
    
    void disapprove() {
        if (!MsgBox.confirm("You are about to disapprove this document. Continue?")) return;
        
        entity = service.disapprove(entity);
    }
    
    def close() {
        return "_close";
    }
}

