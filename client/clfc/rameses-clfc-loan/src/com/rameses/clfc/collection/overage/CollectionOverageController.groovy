package com.rameses.clfc.collection.overage;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class CollectionOverageController 
{
    @Caller
    def caller;

    @Binding
    def binding;

    @ChangeLog
    def changeLog;

    @Service("LoanCollectionOverageService")
    def service;

    def collector;
    def remittanceid;
    def txndate;
    def entity;
    def mode = 'read';
    def afterApproveHandler;

    String title = "Overage";
    String entityName = "overage";

    void init() {
        entity = [
            objid       : 'OVRG' + new UID(),
            state       : 'DRAFT',
            collector   : collector,
            remittanceid: remittanceid,
            txndate     : txndate
        ];
        mode = 'create';
    }

    void open() {
        entity = service.open(entity);
    }

    void save() {
        if (MsgBox.confirm("You are about to save document. Continue?")) {
            if (mode == 'create') {
                entity = service.save(entity);
            } else if (mode == 'edit') {
                entity = service.update(entity);
            }
            mode = 'read';
            binding.refresh('formActions');
            EventQueue.invokeLater({ caller?.reload(); });
        }
    }

    void edit() {
        mode = 'edit';
        binding.refresh('formActions');
    }

    void cancel() {
        if (MsgBox.confirm("Cancelling will undo the changes made. Continue?")) {
            mode = 'read';
            if (changeLog.hasChanges()) {
                changeLog.undoAll();
                changeLog.clear();
            }
            binding.refresh('formActions');
        }
    }

    void submitForNoting() {
        if (!MsgBox.confirm('You are about to submit this document for noting. Continue?')) return;
        
        entity = service.submitForNoting(entity);
        EventQueue.invokeLater({ caller?.reload(); });
    }
    /*
    void submitForSignatory() {
        if (!MsgBox.confirm("You are about to submit this document for signatory. Continue?")) return;
        
        entity = service.submitForSignatory(entity);
        EventQueue.invokeLater({ caller?.reload(); });
    }
    */
    
    void noted() {
        if (!MsgBox.confirm("You are about to confirm this document. Continue?")) return;
        
        entity = service.noted(entity);
        if (afterApproveHandler) afterApproveHandler(entity);
        EventQueue.invokeLater({ caller?.reload(); });
    }
    
    def voidOverage() {
        def handler = { remarks->
            try {
                entity.remarks = remarks;
                entity = service.voidOverage(entity);
                EventQueue.invokeLater({
                     binding?.refresh();
                     caller?.reload();
                });
            } catch (Throwable t) {
                MsgBox.err(t.message);
            }
        }
        return Inv.lookupOpener('remarks:create', [title: "Reason for Void", handler: handler]);
    }
    
    def viewVoidRemarks() {
        return Inv.lookupOpener('remarks:open', [title: 'Reason for Void', remarks: entity.voidremarks]);
    }
    
    /*
    void submitForApproval() {
        if (MsgBox.confirm("You are about to submit this document for approval. Continue?")) {
            entity = service.submitForApproval(entity);
            binding.refresh('formActions');
            EventQueue.invokeLater({ caller?.reload(); });
        }
    }

    void approve() {
        if (MsgBox.confirm("You are about to approve document. Continue?")) {
            entity = service.approveDocument(entity);
            if (afterApproveHandler) afterApproveHandler(entity);
            binding.refresh('formActions');
            EventQueue.invokeLater({ caller?.reload(); });
        }
    }

    void disapprove() {
        if (MsgBox.confirm("You are about to disapprove document. Continue?")) {
            entity = service.disapprove(entity);
            binding.refresh('formActions');
            EventQueue.invokeLater({ caller?.reload(); });
        }
    }
    */

    def close() {
        return "_close";
    }
}