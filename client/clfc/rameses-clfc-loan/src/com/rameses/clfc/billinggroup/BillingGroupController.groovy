package com.rameses.clfc.billinggroup

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class BillingGroupController extends CRUDController
{
    @Binding
    def binding;
    
    @Service('DateService')
    def dateSvc;
    
    String serviceName = 'LoanBillingGroupService';
    
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = true;
    
    def options;
    
    Map createEntity() {
        def date = dateSvc.getServerDateAsString().split(' ')[0];
        return [
            objid       : 'BG' + new UID(),
            txnstate    : 'DRAFT',
            dtstarted   : date
        ];
    }
    
    
    def selectedLedger, selectedLedgerToAdd = [:];
    def listHandler = [
        fetchList: { o->
            if (!entity.items) entity.items = [];
            return entity.items;
        }
    ] as BasicListModel;
    
    void addLedger() {
        if (!selectedLedgerToAdd) throw new Exception('Please select a ledger to add.');
        
        def i = entity?.items?.find{ it.ledgerid == selectedLedgerToAdd.ledger.objid }
        if (i) throw new Exception('This ledger has already been selected.');
                
        def item = [:];
        item.putAll(selectedLedgerToAdd);
        item.objid = 'BGD' + new UID();
        item.parentid = entity.objid;
        item.ledgerid = item.ledger.objid;
        
        if (!entity._addedledgers) entity._addedledgers = [];
        entity._addedledgers.add(item);
        
        if (!entity.items) entity.items = [];
        entity.items.add(item);
        
        listHandler?.reload();
    }
    
    void removeLedger() {
        if (!MsgBox.confirm('You are about to remove this ledger. Continue?')) return;
        
        if (!entity._removedledgers) entity._removedledgers = [];
        entity._removedledgers.add(selectedLedger);
        
        if (entity._addedledgers) entity._addedledgers.remove(selectedLedger);
        entity.items.remove(selectedLedger);
        
        listHandler?.reload();
    }
    
    void afterOpen( data ){
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
        selectedLedgerToAdd = [:];
        listHandler?.reload();
        binding?.refresh('options');
    }
    
    void afterSave( data ) {
        data._removedledgers = [];
        data._addedledgers = [];
    }
    
    def getTypeList() {
        return service.getTypes();
    }
    
    def getOptionsList() {
        getOpeners: {
            if (!selectedLedgerToAdd) selectedLedgerToAdd = [:];
            def list = Inv.lookupOpeners("billinggroup-option",[selectedLedgerToAdd: selectedLedgerToAdd]);
            
            list?.sort{ it.properties.index }
            return list;
        }
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
    
}


/*
class BillingGroupController extends CRUDController
{
    @Binding
    def binding;
    
    @Service("DateService")
    def dateSvc;
    
    String serviceName = "LoanBillingGroupService";
    String entityName = "billinggroup";
    
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = true;
    
    Map createPermission = [domain: 'LOAN', role: 'LEGAL_OFFICER,ACCT_ASSISTANT'];
    Map editPermission = [domain: 'LOAN', role: 'LEGAL_OFFICER,ACCT_ASSISTANT'];
    
    def selectedLedger, prevledgers;
        
    Map createEntity() {
        def date = dateSvc.getServerDateAsString().split(" ")[0];
        return [
            objid       : 'BG' + new UID(),
            txnstate    : 'DRAFT',
            dtstarted   : date,
            dtended     : date
        ];
    }
    
    void afterCreate( data ) {
        listHandler?.reload();
    }
    
    def getTypeList() {
        def list = service.getTypes();
        if (!list) list = [];
        return list;
    }
    
    void afterOpen( data ) {
        checkEditable(data);
    }
        
    void afteSave( data ) {
        data._addedledger = [];
        data._removedledger = [];
        listHandler?.reload();
    }
    
    void afterEdit( data ) {
        prevledgers = [];
        def item;
        data.ledgers.each{ o->
            item = [:];
            item.putAll(o);
            prevledgers.add(item);
        }
    }
    
    void afterCancel() {
        entity.ledgers = [];
        entity.ledgers.addAll(prevledgers);
        listHandler?.reload();
    }
    
    def getBorrowerLookupHandler() {        
        def handler = { o->
            if (entity.ledgers.find{ o.objid == it.ledgerid }) {
                throw new Exception("Ledger has already been selected.");
            }
            
            if (!entity._addedledger) entity._addedledger = [];
            def item = [
                objid   : 'BGD' + new UID(),
                parentid: entity.objid,
                state   : o.state,
                ledgerid: o.objid,
                borrower: [objid: o.acctid, name: o.acctname],
                route   : o.route
            ];
            
            
            if (!selectedLedger) {
                selectedLedger = item;
                entity.ledgers.add(item);
                entity._addedledger.add(item);
            } else {
                item.objid = selectedLedger.objid;
                item._edited = true;
                def i = entity._addedledger?.find{ it.objid == selectedLedger.objid }
                if (i) i = item;
                
                selectedLedger.clear();
                selectedLedger.putAll(item);
            }
            listHandler.reload();
        }
       return Inv.lookupOpener('specialcollectionledger:lookup', [onselect: handler]);
    }
    
    void checkEditable( data ) {
        if (data.txnstate!='DRAFT') {
            allowEdit = false;
        }
        binding?.refresh('formActions');
    }
    
    def listHandler = [
        fetchList: { o->
            if (!entity.ledgers) entity.ledgers = [];
            return entity.ledgers;
        }
    ] as EditorListModel;
    
    void removeLedger() {        
        if (!MsgBox.confirm("You are about to remove this ledger. Continue?")) return;

        if (!entity._removedledger) entity._removedledger = [];
        entity._removedledger.add(selectedLedger);

        if (entity._addedledger) entity._addedledger.remove(selectedLedger);
        entity.ledgers.remove(selectedLedger);
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
    }
    
    void disapprove() {
        if (!MsgBox.confirm("You are aobut to disapprove this document. Continue?")) return;
        
        entity = service.disapprove(entity);
    }
    
}
*/