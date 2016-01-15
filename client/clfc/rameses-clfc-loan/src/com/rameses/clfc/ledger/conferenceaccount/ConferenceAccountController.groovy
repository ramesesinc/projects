package com.rameses.clfc.ledger.conferenceaccount

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class ConferenceAccountController extends CRUDController
{
    @Binding
    def binding;
    
    String serviceName = 'LoanConferenceAccountService';
    
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = true;
    
    Map createEntity() {
        return [objid: 'CFA' + new UID(), txnstate: 'DRAFT'];
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
    
    void afterCreate( data ) {
        data.items = [];
        listHandler?.reload();
    }
    
    def previtems;
    void afterEdit( data ) {
        if (data.items) {
            previtems = [];
            def item;
            data?.items?.each{ o->
                item = [:];
                item.putAll(o);
                previtems.add(item);
            }
        }
    }
    
    void afterCancel() {
        if (previtems) {
            entity.items = [];
            entity.items.addAll(previtems);
            listHandler?.reload();
        }
        entity._addeditems = [];
        entity._removeditems = [];
    }
    
    void afterSave( data ) {
        data._addeditems = [];
        data._removeditems = [];
    }
    
    def selectedItem;
    def listHandler = [
        fetchList: { o->
            if (!entity.items) entity.items = [];
            return entity.items;
        }
    ] as BasicListModel;
    
    def addItem() {
        def handler = { o->
            def i = entity.items?.find{ o.objid == it.ledgerid }
            if (i) throw new Exception('Ledger for ' + o.borrower.name + ' with App. No. ' + o.loanapp.appno + ' has already been selected.');
            
            def item = [
                objid   : 'CFAD' + new UID(),
                parentid: entity?.objid,
                borrower: o.borrower,
                loanapp : o.loanapp,
                ledger  : [
                    objid       : o.objid,
                    dtreleased  : o.dtreleased,
                    dtmatured   : o.dtmatured
                ]
            ]
            
            if (!entity._addeditems) entity._addeditems = [];
            entity._addeditems.add(item);
            
            entity.items.add(item);
            
            listHandler?.reload();
        }
        def op = Inv.lookupOpener('conferenceaccount:ledger:lookup', [onselect: handler]);
        if (!op) return null;
        return op;
    }
    
    void removeItem() {
        if (!MsgBox.confirm('You are about to remove this item. Continue?')) return;
        
        if (!entity._removeditems) entity._removeditems = [];
        entity._removeditems.add(selectedItem);
        
        if (entity._addeditems) entity._addeditems.remove(selectedItem);
        entity.items.remove(selectedItem);
        listHandler?.reload();
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
        
        entity = service.closeAccount(entity);
        checkEditable(entity);
    }
}

