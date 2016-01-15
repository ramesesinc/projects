package com.rameses.clfc.patch.loan.close

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class CloseLoanController extends CRUDController
{
    @Binding
    def binding;
    
    String serviceName = "CloseLoanService";
    
    Map createPermission = [domain: 'LOAN', role: 'CAO_OFFICER,ACCT_ASSISTANT,CASHIER'];
    Map editPermission = [domain: 'LOAN', role: 'CAO_OFFICER,ACCT_ASSISTANT,CASHIER'];
    
    boolean allowDelete = false;
    boolean allowApprove = false;
    boolean allowEdit = true;
    
    Map createEntity() {
        return [objid: 'CL' + new UID(), txnstate: 'DRAFT'];
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
    
    def selectedLedger;
    def listHandler = [
        fetchList: { o->
            if (!entity.items) entity.items = [];
            return entity.items;
        }
    ] as BasicListModel
    
    def prevledgers;
    void afterEdit( data ) {
        prevledgers = [];
        def item;
        data.items.each{ o->
            item = [:];
            item.putAll(o);
            prevledgers.add(item);
        }
        entity.remove('_added');
        entity.remove('_removed');
    }
    
    void afterCancel() {
        entity.items = [];
        entity.items.addAll(prevledgers);
        listHandler?.reload();
    }
    
    def addLedger() {
        def handler = { o->
            def i = entity.items.find{ it.loanapp.objid == o.loanapp.objid }
            if (i) throw new Exception("Loan app record already selected.");
            
            def item = [
                objid       : 'CLD' + new UID(),
                parentid    : entity.objid,
                borrower    : o.borrower,
                loanapp     : o.loanapp,
                loanamount  : o.loanamount,
                dtreleased  : o.dtreleased,
                dtmatured   : o.dtmatured
            ];
            
            if (!entity._added) entity._added = [];
            entity._added.add(item);
            entity.items.add(item);
            
            listHandler?.reload();
        }
        return Inv.lookupOpener('forcloseloanapp:lookup', [onselect: handler]);
    }
    
    void removeLedger() {
        if (!MsgBox.confirm("You are about to remove this ledger. Continue?")) return;
        
        entity.items.remove(selectedLedger);
        if (entity._added) entity._added.remove(selectedLedger);
        
        if (!entity._removed) entity._removed = [];
        entity._removed.add(selectedLedger);
        
        listHandler?.reload();
    }
    
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

