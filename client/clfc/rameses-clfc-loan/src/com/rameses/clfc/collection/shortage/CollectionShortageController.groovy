package com.rameses.clfc.collection.shortage;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class CollectionShortageController
{
    @Caller
    def caller;

    @Binding
    def binding;

    @ChangeLog
    def changeLog;

    @Service("LoanCollectionShortageService")
    def service;

    def collector, remittanceid, txndate, entity;
    def mode = 'read', cbsmode = 'read';
    def action, totalbreakdown = 0;
    def afterApproveHandler;
    def prevcashbreakdown;
    
    String title = "Shortage";
    String entityName = "shortage";

    void init() {
        entity = [
            objid           : 'SHRTG' + new UID(),
            state           : 'DRAFT',
            collector       : collector,
            remittanceid    : remittanceid,
            txndate         : txndate
        ];
        mode = 'create';
    }

    def close() {
        return "_close";
    }

    void open() {
        entity = service.open(entity);
        if (entity.state == 'NOTED') {
            action = 'cashbreakdown';
            if (!entity.cashbreakdown) {
                entity.cashbreakdown = createCashBreakdown();
            }
        }
        totalbreakdown = 0;
        if (entity.cashbreakdown?.items) totalbreakdown = entity.cashbreakdown.items.amount.sum();
        mode = 'read';
    }    

    void save() {
        if (!MsgBox.confirm('You are about to save this document. Continue?')) return;
        
        if (mode == 'create') {
            entity = service.save(entity);
        } else if (mode == 'edit') {
            entity = service.update(entity);
        }
        mode = 'read';
        EventQueue.invokeLater({ caller?.reload(); });
        /*
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
        */
    }
    
    void edit() {
         mode = 'edit';
        
        /*
        mode = 'edit'
        binding.refresh('formActions');
        */
    }

    void cancel() {
        if (!MsgBox.confirm('Cancelling will undo the changes made. Continue?')) return;
        if (changeLog.hasChanges()) {
            changeLog.undoAll();
            changeLog.clear();
        }
        mode = 'read';
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
        if (!entity.cashbreakdown) entity.cashbreakdown = createCashBreakdown();
        if (afterApproveHandler) afterApproveHandler(entity);
        EventQueue.invokeLater({ caller?.reload(); });
    }
    
    def voidShortage() {
        def handler = { remarks->
            try {
                entity.remarks = remarks;
                entity = service.voidShortage(entity);
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
    
    private def createCashBreakdown() {
        def m = [
            objid   : 'CB' + new UID(),
            items   : []
        ];
        totalbreakdown = 0;
        return m;
    }

    def getCashbreakdown() {
        def params = [
            entries         : (entity.cashbreakdown? entity.cashbreakdown.items : []),
            totalbreakdown  : totalbreakdown,
            editable        : (cbsmode!='read' && entity?.alloweditcbs==true),
            onupdate        : {o->
                totalbreakdown = o;
            }
        ]
        return InvokerUtil.lookupOpener('clfc:denomination', params);
    }
    
    void editCbs() {
        prevcashbreakdown = [];
        def item;
        entity?.cashbreakdown?.items?.each{ o->
            item = [:];
            item.putAll(o);
            prevcashbreakdown.add(item);
        }
        cbsmode = 'edit';
    }
    
    void cancelCbs() {
        if (!MsgBox.confirm('Cancelling will undo the changes made. Continue?.')) return;
        
        if (entity?.cashbreakdown) {
            entity.cashbreakdown.items = [];
            entity.cashbreakdown.items.addAll(prevcashbreakdown);
        }
        totalbreakdown = (entity.cashbreakdown?.items? entity.cashbreakdown.items.amount.sum() : 0);

        if (changeLog.hasChanges()) {
            changeLog.undoAll();
            changeLog.clear();
        }
        cbsmode = 'read';
    }
    
    void saveCbs() {
        if (totalbreakdown != entity?.amount) {
            throw new Exception('Total for cash breakdown is not equal to amount.');
        }
        
        if (!MsgBox.confirm('You are about to save cash breakdown. Continue?')) return;
        
        entity.cashbreakdown.cbsno = entity.cbsno;
        entity.cashbreakdown = service.updateCashBreakdown(entity);
        cbsmode = 'read';
    }

    /*
    void saveCashbreakdown() {
        if (totalbreakdown != entity.amount) 
            throw new Exception("Total for cash breakdown is not equal to amount.");
    
        if (MsgBox.confirm("You are about to save cash breakdown. Continue?")) {
            entity.cashbreakdown.cbsno = entity.cbsno;
            if (mode == 'create') {
                entity.cashbreakdown = service.saveCashbreakdown(entity);
            } else if (mode == 'edit') {
                entity.cashbreakdown = service.updateCashbreakdown(entity);
            }
            mode = 'read';
            binding.refresh('formActions');
        }
    }

    void editCashbreakdown() {
        mode = 'edit';
        prevcashbreakdown = [];
        def map;
        entity.cashbreakdown?.items.each{ o->
            map = [:];
            map.putAll(o);
            prevcashbreakdown << map;
        }
        binding.refresh('formActions');
    }

    boolean getAllowEdit() {
        def flag = false;
        //if (mode=='read' && (entity.state=='APPROVED' || entity.cashbreakdown?.state=='SEND_BACK'))
        if (mode=='read' && entity.state!='DRAFT' && (entity.allowEdit==true || entity.cashbreakdown?.state=='SEND_BACK') && !entity.voidremarks)
            flag = true;
        return flag;
    }
    
    boolean getAllowCancel() {
        def flag = false;
        //if (mode=='edit' && (entity.state=='APPROVED' || entity.cashbreakdown?.state=='SEND_BACK'))
        if (mode=='edit' && entity.state!='DRAFT' && (entity.allowEdit==true || entity.cashbreakdown?.state=='SEND_BACK'))
            flag = true;
        return flag;
    }
    
    boolean getAllowSave() {
        def flag = false;
        //if (mode!='read' && (entity.state=='APPROVED' || entity.cashbreakdown?.state=='SEND_BACK'))
        if (mode!='read' && entity.state!='DRAFT' && (entity.allowEdit==true || entity.cashbreakdown?.state=='SEND_BACK'))
            flag = true;
        return flag;
    }
    
    boolean getAllowViewSendback() {
        def flag = false;
        if (mode=='read' && entity.cashbreakdown.state=='SEND_BACK') flag = true;
        return flag;
    }
    
    boolean getAllowCbsForVerification() {
        def flag = false;
        if (mode=='read' && entity.amount == totalbreakdown && entity.cashbreakdown?.state=='SEND_BACK') flag = true;
        return flag;
    }
    
    void submitCbsForVerification() {
        if (!MsgBox.confirm("You are about to submit CBS for this document for verification. Continue?")) return;
        
        entity.cashbreakdown = service.submitCbsForVerification(entity);
        entity.allowEdit = false;
        binding.refresh();
    }
    
    def viewCbsSendback() {
        return Inv.lookupOpener('remarks:open', [title: "Reason for Send Back", remarks: entity.cashbreakdown?.sendbackremarks]);
    }
    
    void cancelCashbreakdown() {
        if (MsgBox.confirm("Cancelling will undo the changes made. Continue?")) {
            mode = 'read';
            
            if (entity.cashbreakdown) {
                if (!entity.cashbreakdown.items) entity.cashbreakdown.items = [];
                entity.cashbreakdown.items.clear();
                entity.cashbreakdown.items.addAll(prevcashbreakdown);
            }
            totalbreakdown = (entity.cashbreakdown?.items? entity.cashbreakdown.items.amount.sum() : 0);

            if (changeLog.hasChanges()) {
                changeLog.undoAll();
                changeLog.clear();
            }
            binding.refresh('formActions');
        }
    }
    */
}