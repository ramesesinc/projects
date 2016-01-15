package com.rameses.clfc.treasury.amnesty;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.framework.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class AmnestyController 
{
    @Binding
    def binding;
    
    @Caller
    def caller;
    
    @Service("DateService")
    def dateSvc;
    
    @Service("AmnestyService")
    def service;
        
    boolean allowCreate = true;
    boolean allowClose = true;

    String title = 'Amnesty';
    String entityName = "amnesty";
    
    def option;
    def borrower;
    def mode = 'read';
    def forApprovalMode = 'read';
    def approvalMode = 'read';
    def prevoffers;
    def prevgrantedoffer;
    def allowAmend = false;
    def entity;

    @PropertyChangeListener
    def listener = [
        "option": {o ->
            entity.amnestyoption = o.caption;
        }
    ];
    
    @ChangeLog
    def changeLog;
    
    
    
    void create() {
        mode = 'create';
        entity = [
            objid           : 'AMNSTY' + new UID(),
            txnstate        : 'DRAFT',
            txnmode         : 'ONLINE',
            txndate         : dateSvc.getServerDateAsString().split(" ")[0],
            iswaivepenalty  : 0,
            iswaiveinterest : 0,
            version         : 1
        ];
        option = null;
        binding?.refresh('option|formActions');
    }
    
    def borrowerLookup = Inv.lookupOpener('ledgerborrower:lookup', [
        onselect: { o->
           entity.borrower = o.borrower;
           entity.loanapp = o.loanapp;
           entity.ledgerid = o.objid;
        },     
        pastdueledgers: true  
    ]);

    def options = [
        [name: 'waiver', caption: 'WAIVER'],
        [name: 'fix', caption: 'FIX'],
        /*[name: "waiveoption:display", caption:"waive"],
        [name:"fixoption:display", caption:"fix"]*/
    ];
    def getIsFla() {
        def flag = false;
        if (ClientContext.currentContext.headers.ROLES.containsKey("TREASURY.FLA")) 
            flag = true;
            
        return flag;
    }
    def getOpener() {
        if (!option) return;
        def params = [
            entity          : entity,
            allowEdit       : (mode != 'read'? true : false),
            forApprovalMode : forApprovalMode,
            approvalMode    : approvalMode,
            allowAmend      : allowAmend
        ];
        return Inv.lookupOpener(option.name + ":option", params);
    }
    
    void open() {
        entity = service.open(entity);
        //if (entity.txnstate != 'DRAFT') allowEdit = false;
        option = options.find{ it.caption == entity.amnestyoption }
                
    }

    void afterOpen( data ) {
        if (data.txnstate != 'DRAFT') allowEdit = false;
        option = options.find{ it.caption == entity.amnestyoption }
    }
    
    void afterCreate( data ) {
        option = null;
        binding?.refresh('option');
    }
    
    void edit() {
        mode = 'edit';
        prevoffers = [];
        def item;
        entity.offers.each{ o->
            item = [:];
            item.putAll(o);
            prevoffers.add(item);
        }
        prevgrantedoffer = [:];
        prevgrantedoffer.putAll(entity.grantedoffer);
        allowClose = false;
        allowCreate = false;
        binding.refresh('formActions');
    }
    
    void cancel() {
        if (!MsgBox.confirm("Changes will be discarded. Continue?")) return;
        
        mode = 'read';
        entity.offers = prevoffers;
        if (changeLog.hasChanges()) {
            changeLog.undoAll();
            changeLog.clear();
        }
        entity.grantedoffer = prevgrantedoffer;
        allowClose = true;
        allowCreate = true;
        binding.refresh('formActions');
    }
    
    void save() {
        if (!MsgBox.confirm("You are about to save this document. Continue?")) return;
        
        def m = mode;
        if (m == 'create') {
            entity = service.save(entity);
        } else {
            entity = service.update(entity);
        }
        mode = 'read';
        allowClose = true;
        allowCreate = true;
        binding.refresh('formActions');
        changeLog.clear();
    }
    
    void submitForApproval() {
        if (!MsgBox.confirm("You are about to submit this request for approval. Continue?")) return;
        
        entity = service.submitForApproval(entity);
    }
    
    void editForApproval() {
        forApprovalMode = 'edit';
        prevoffers = [];
        def item;
        entity.offers.each{ o->
            item = [:];
            item.putAll(o);
            prevoffers.add(item);
        }
        prevgrantedoffer = [:];
        prevgrantedoffer.putAll(entity.grantedoffer);
        allowClose = false;
        allowCreate = false;
        binding.refresh('formActions');
    }
    
    void cancelForApproval() {
        forApprovalMode = 'read';
        if (changeLog.hasChanges()) {
            changeLog.undoAll();
            changeLog.clear()
        }
        entity.offers = prevoffers;
        entity.grantedoffer = prevgrantedoffer;
        allowClose = true;
        allowCreate = true;
        binding.refresh('formActions');
    }
    
    void saveForApproval() {
        def m = forApprovalMode;
        if (m == 'create') {
            entity  = service.create(entity);
        } else if (m == 'edit') {
            entity = service.update(entity);
        }
        forApprovalMode = 'read';
        allowClose = true;
        allowCreate = true;
        binding.refresh('formActions');
        changeLog.clear();
    }
    
    def approveDocument() {
        if (!MsgBox.confirm("You are about to approve this document. Continue?")) return;
                
        return Inv.lookupOpener("remarks:create", [
            title: 'Remarks for approval', 
            handler: { remarks-> 
                try {                    
                    entity.posterremarks = remarks;
                    entity = service.approveDocument(entity);
                    EventQueue.invokeLater({ 
                        caller?.reload(); 
                        binding?.refresh();
                    });
                } catch (Throwable t) {
                    MsgBox.err(t.message);
                }
            } 
        ])
    }
    
    def disapprove() {
        if (!MsgBox.confirm("You are about to disapprove this document. Continue?")) return;
        
        return Inv.lookupOpener("remarks:create", [
            title: 'Remarks for disapproval', 
            handler: { remarks-> 
                try {                    
                    entity.posterremarks = remarks;
                    entity = service.disapprove(entity);
                    EventQueue.invokeLater({ 
                        caller?.reload(); 
                        binding?.refresh();
                    });
                } catch (Throwable t) {
                    MsgBox.err(t.message);
                }
            } 
        ])
    }
    
    void editApproval() {
        approvalMode = 'edit';
        prevoffers = [];
        def item;
        entity.offers.each{ o->
            item = [:];
            item.putAll(o);
            prevoffers.add(item);
        }
        prevgrantedoffer = [:];
        prevgrantedoffer.putAll(entity.grantedoffer);
        allowClose = false;
        allowCreate = false;
        binding.refresh('formActions');
    }
    
    void cancelApproval() {
        approvalMode = 'read';
        entity.offers = prevoffers;
        entity.grantedoffer = prevgrantedoffer;
        allowClose = true;
        allowCreate = true;
        binding.refresh('formActions');
    }
    
    void saveApproval() {
        def m = approvalMode;
        if (m == 'create') {
            entity  = service.create(entity);
        } else if (m == 'edit') {
            entity = service.update(entity);
        }
        approvalMode = 'read';
        allowClose = true;
        allowCreate = true;
        binding.refresh('formActions');
        changeLog.clear();
    }
    
    void avail() {
        if (!MsgBox.confirm("You are about to avail this document. Continue?")) return;
        
        entity = service.avail(entity);
    }
    
    def reject(){
       return Inv.lookupOpener('remarks:create', [
            title: 'Remarks for Rejection', 
            handler: {remarks-> 
                entity.remarks = remarks;
            } 
        ]);
    }
    
    void amend() {
        allowAmend = true;
        prevoffers = [];
        def item;
        entity.offers.each{ o->
            item = [:];
            item.putAll(o);
            prevoffers.add(item);
        }
        prevgrantedoffer = [:];
        prevgrantedoffer.putAll(entity.grantedoffer);
        allowClose = false;
        allowCreate = false;
        binding.refresh('formActions');
        changeLog.clear();
    }
    
    void cancelAmend() {
        allowAmend = false;
        if (changeLog.hasChanges()) {
            changeLog.undoAll();
            changeLog.clear();
        }
        entity.offers = prevoffers;
        entity.grantedoffer = prevgrantedoffer;
        allowClose = true;
        allowCreate = true;
        binding.refresh('formActions');
    }
    
    void saveAmend() {
        allowAmend = false;
        
        entity = service.amend(entity);
        allowClose = true;
        allowCreate = true;
        binding.refresh('formActions');
   }
    
    def getAllowForAmend() {
        def flag = false;
        if (!entity.foramend) entity.foramend = false;
        if (entity.txnstate=='APPROVED' && approvalMode=='read' && entity.amnestyoption=='FIX' && allowAmend==false && entity.foramend==true) flag = true;
        return flag;
    }
    
    def getAllowAvail() {
        def flag = false;
        if (entity.txnstate=='APPROVED' && approvalMode=='read' && entity.amnestyoption=='FIX' && !entity.txntype) flag = true;
        return flag;
    }
    
    def getAllowReject() {
        return getAllowAvail();
    }
    
    def getAllowClose() {
        return allowClose;
    }
    
    def close() { return "_close"; }
    
}

