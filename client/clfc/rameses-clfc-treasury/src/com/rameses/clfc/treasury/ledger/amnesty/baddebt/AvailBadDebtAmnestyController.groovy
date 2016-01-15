package com.rameses.clfc.treasury.ledger.amnesty.baddebt

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;


class AvailBadDebtAmnestyController extends CRUDController
{
    @Binding
    def binding;
    
    def amnestytype = 'BADDEBT';
    
    String serviceName = 'AvailBadDebtAmnestyService';
    String entityName = 'availbaddebtamnesty';
    
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = true;
    
    Map createPermission = [domain: 'LOAN', role: 'CAO_OFFICER,LEGAL_OFFICER'];
    Map editPermission = [domain: 'LOAN', role: 'CAO_OFFICER,LEGAL_OFFICER'];
    
    def amnestyLookup = Inv.lookupOpener('ledgeramnesty:lookup', [
         onselect   : { o->
             //println 'selected amnesty ' + o;
             def am = service.getAmnestyInfo(o);
             if (am) {
                 entity.amnesty = am;
                 entity.amnestyid = am.objid;
                 entity.borrower = am.borrower;
                 entity.loanapp = am.loanapp;
                 entity.ledger = am.ledger;
                 entity.refid = am.baddebt.refid;
                 entity.amount = am.baddebt.amount;
                 entity.description = am.baddebt.description;
             }
             binding?.refresh();
         },
         type       : amnestytype,
         foravail   : true
    ]);

    /*
    def noncash, noncashmode = 'read';
    def collectorLookup = Inv.lookupOpener('cashier:lookup', [
         onselect: { o->
            if (!noncash) noncash = [:];
            noncash.collector = o;
            binding?.refresh();
         }
    ]);
    */
    
    Map createEntity() {
        return [
            objid   : 'AF' + new UID(),
            txnstate: 'DRAFT'
        ];
    }
    
    /*
    void afterCreate( data ) {
        noncashmode = 'read';
        noncash = [:];
        binding?.refresh();
    }
    */
    
    void afterOpen( data ) {
        checkEditable(data);
        //if (data.noncash) noncash = data.noncash;
    }
    
    void checkEditable( data ) {
        allowEdit = false;
        if (data.txnstate == 'DRAFT') {
            allowEdit = true;
        }
        binding?.refresh('formActions');
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
    
    void submitForVerification() {
        if (!MsgBox.confirm('You are about to submit this document for verification. Continue?')) return;
        
        entity = service.submitForVerification(entity);
        checkEditable(entity);
    }
    
    void verify() {
        if (!MsgBox.confirm('You are about to verify this document. Continue?')) return;
        
        entity = service.verify(entity);
        checkEditable(entity);
    }

    /*
    def prevnoncash;
    void editNoncash() {
        allowCreate = false;
        if (noncash) {
            prevnoncash = [:];
            prevnoncash.putAll(noncash);
        }
        noncashmode = 'edit';
    }
    
    void cancelNoncash() {
        if (!MsgBox.confirm('Cancelling will undo changes made. Continue?')) return;
        
        allowCreate = true;
        noncash = prevnoncash;
        noncashmode = 'read';
        binding?.refresh();
    }
    
    void saveNoncash() {
        if (noncash) entity.noncash = noncash;
        entity = service.update(entity);
        checkEditable(entity);
        
        if (entity.noncash) noncash = entity.noncash;
        noncashmode = 'read'
        allowCreate = true;
        binding?.refresh();
    }
    */
}

/*
class AvailBadDebtAmnestyController
{
    @Binding
    def binding;
    
    @Service('AvailBadDebtAmnestyService')
    def service;
    
    @ChangeLog
    def changeLog;
    
    def mode = 'read', entity;
    
    def amnestyLookup = Inv.lookupOpener('ledgeramnesty:lookup', [
         onselect   : { o->
             //println 'selected amnesty ' + o;
             def am = service.getAmnestyInfo(o);
             if (am) {
                 entity.amnesty = am;
                 entity.amnestyid = am.objid;
                 entity.borrower = am.borrower;
                 entity.loanapp = am.loanapp;
                 entity.ledger = am.ledger;
                 entity.refid = am.baddebt.refid;
                 entity.amount = am.baddebt.amount;
                 entity.description = am.baddebt.description;
             }
             binding?.refresh();
         },
         type       : amnestytype,
         foravail   : true
    ]);

    def noncash, noncashmode = 'read';
    def collectorLookup = Inv.lookupOpener('cashier:lookup', [
         onselect: { o->
            if (!noncash) noncash = [:];
            noncash.collector = o;
            binding?.refresh();
         }
    ]);

    void create() {
        entity = createEntity();
        mode = 'create'
        noncashmode = 'read';
        noncash = [:];
        binding?.refresh();
    }
    
    Map createEntity() {
        return [
            objid   : 'AF' + new UID(),
            txnstate: 'DRAFT'
        ];
    }
    
    void open() {
        entity = service.open(entity);
        mode = 'read';
        if (entity.noncash) noncash = entity.noncash;
    }
   
    def cancel() {
        if (mode == 'edit') {
            
            
            
            mode = 'read';
            return null;
        }
        return '_close';
    }
    
    void submitForApproval() {
        if (!MsgBox.confirm('You are about to submit this document for approval. Continue?')) return;
        
        entity = service.submitForApproval(entity);
        //checkEditable(entity);
    }
    
    void approveDocument() {
        if (!MsgBox.confirm('You are about to approve this document. Continue?')) return;
        
        entity = service.approveDocument(entity);
        //checkEditable(entity);
    }
    
    void disapprove() {
        if (!MsgBox.confirm('You are about to disapprove this document. Continue?')) return;
        
        entity = service.disapprove(entity);
        //checkEditable(entity);
    }
    
    void submitForVerification() {
        if (!MsgBox.confirm('You are about to submit this document for verification. Continue?')) return;
        
        entity = service.submitForVerification(entity);
        //checkEditable(entity);
    }
    
    void verify() {
        if (!MsgBox.confirm('You are about to verify this document. Continue?')) return;
        
        entity = service.verify(entity);
        //checkEditable(entity);
    }
    
    def prevnoncash;
    void editNoncash() {
        allowCreate = false;
        if (noncash) {
            prevnoncash = [:];
            prevnoncash.putAll(noncash);
        }
        noncashmode = 'edit';
    }
    
    void cancelNoncash() {
        if (!MsgBox.confirm('Cancelling will undo changes made. Continue?')) return;
        
        allowCreate = true;
        noncash = prevnoncash;
        noncashmode = 'read';
        binding?.refresh();
    }
    
    void saveNoncash() {
        if (noncash) entity.noncash = noncash;
        entity = service.update(entity);
        checkEditable(entity);
        
        if (entity.noncash) noncash = entity.noncash;
        noncashmode = 'read'
        allowCreate = true;
        binding?.refresh();
    }
}
*/
