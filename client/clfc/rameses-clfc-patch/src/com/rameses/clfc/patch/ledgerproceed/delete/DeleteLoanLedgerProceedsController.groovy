package com.rameses.clfc.patch.ledgerproceed.delete

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class DeleteLoanLedgerProceedsController extends CRUDController
{
    @Binding
    def binding;
    
    String serviceName = "DeleteLedgerProceedsService";
    
    boolean allowDelete = false;
    boolean allowApprove = false;
    boolean allowEdit = true;
    
    String prefix = 'DLP';
    
    void afterOpen( data ) {
        checkEditable(data);
    }
    
    void checkEditable( data ) {
        allowEdit = true;
        if (data.state != 'DRAFT') {
            allowEdit = false;
        }
        binding?.refresh('formActions');
    }
    
    def proceeds;
    def proceedsLookup = Inv.lookupOpener('fordelete-proceeds:lookup', [
        onselect: { o->
            entity.proceeds = o;
            entity.ledgerid = o.parentid;
            entity.borrower = o.borrower;
            entity.loanapp = o.loanapp;
            entity.refno = o.refno;
            entity.amount = o.amount;
            binding?.refresh();
        },
        state : 'SOLD',
        txntype: 'CAPTURE'
    ]);
    
    
    void submitForApproval() {
        if (!MsgBox.confirm("You are about to submit this document for approval. Continue?")) return;
        
        entity = service.submitForApproval(entity);
        checkEditable(entity);
    }
    
    void approveDocument() {
        if (!MsgBox.confirm("You are about to approve this document. Continue?")) return;
        
        entity = service.approveDocument(entity);
        checkEditable(entity);
    }
    
    void disapprove() {
        if (!MsgBox.confirm("You are about to disapprove this document. Continue?")) return;
        
        entity = service.disapprove(entity);
        checkEditable(entity);
    }
}

