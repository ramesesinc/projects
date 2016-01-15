package com.rameses.clfc.treasury.loan.receivable

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class LoanReceivableCaptureController extends CRUDController 
{
    @Binding
    def binding;
    
    String serviceName = "LoanReceivableCaptureService";
    String entityName = "loanareceivablecapture";
    String domain = 'LOAN';
    String role = 'CAO_OFFICER,ACCT_ASSISTANT';
    
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = true;
    
    def loanapp;
    def loanappLookup = Inv.lookupOpener('loanapp:lookup', [
         onselect: { o->
             if (o.state=='CLOSED') 
                throw new Exception("Loan ${o.appno} for ${o.borrower.name} is already closed.");
             
             entity.loanappid = o.objid;
             entity.loanapp = [objid: o.objid, appno: o.appno];
             entity.borrower = o.borrower;
             binding?.refresh();
         }
    ]);
    
    Map createEntity() {
        return [
            objid   : 'CLR' + new UID(),
            txnstate: 'DRAFT',
            txntype : 'CAPTURE',
            category: 'RECEIVABLE'
        ];
    }
    
    void afterOpen( data ) {
        checkEditable(data);
    }
    
    void checkEditable( data ) {
        if (data.txnstate=='DRAFT') {
            allowEdit = true;
        } else if (data.txnstate!='DRAFT') {
            allowEdit = false;
        }
        binding?.refresh('formActions');
    }
    
    def activate() {
        if (!MsgBox.confirm("You are about to activate this document. Continue?")) return;
        
        entity = service.activate(entity);
        checkEditable(entity);
    }
    
    def voidDocument() {
        def handler = { remarks->
            entity.voidremarks = remarks;
            entity = service.voidDocument(entity);
            checkEditable(entity);
        }
        return Inv.lookupOpener('remarks:create', [title: 'Reason for Void', handler: handler]);
    }
}

