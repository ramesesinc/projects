package com.rameses.clfc.ledger.specialbillingaccount

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class SpecialBillingAccountController extends CRUDController
{
    @Binding
    def binding;
    
    String serviceName = 'LoanSpecialBillingAccountService';
    
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = true;
    
    Map createEntity() {
        return [objid: 'SBA' + new UID(), txnstate: 'DRAFT'];
    }
    
    def getBorrowerLookup() {
        def handler = { o->
            entity.borrower = o.borrower;
            entity.loanapp = o.loanapp;
            entity.ledger = [
                objid       : o.objid,
                dtreleased  : o.dtreleased,
                dtmatured   : o.dtmatured
            ];
            binding?.refresh();
        }
        def op = Inv.lookupOpener('specialbillingaccount:ledger:lookup', [onselect: handler]);
        if (!op) return null;
        return op;
    }
    
    void afterOpen( data ) {
        checkEditable(data);
    }
    
    void checkEditable( data ) {
        allowEdit = false;
        if (data.txnstate == 'DRAFT') {
            allowEdit = true;
        }
        binding?.refresh('formActions');
    }
    
    def preventity;
    void afterEdit( data ) {
        preventity = [:];
        preventity.putAll(data);
        binding?.refresh();
    }
    
    void afterCancel() {
        entity = preventity;
        binding?.refresh();
    }
    
    
    void submitForApproval() {
        if (!MsgBox.confirm('You are about to submit this document for approval. Continue?')) return;
        
        entity = service.submitForApproval(entity);
        checkEditable(entity);
    }
    
    void approveDocument() {
        if (!MsgBox.confirm('You are about to approve this document. Continue?')) return;
        
        entity = service.approveDocument(entity);
        checkEditable(entity);
    }
    
    void disapprove() {
        if (!MsgBox.confirm('You are about to disapprove this document. Continue?')) return;
        
        entity = service.disapprove(entity);
        checkEditable(entity);
    }
    
    void closeAccount() {
        if (!MsgBox.confirm('You are about to close this document. Continue?')) return;
        
        entity = service.closeDocument(entity);
        checkEditable(entity);
    }
    
    
}

