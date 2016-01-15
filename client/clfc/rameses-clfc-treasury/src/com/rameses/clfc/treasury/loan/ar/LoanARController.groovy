package com.rameses.clfc.treasury.loan.ar

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.framework.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import java.rmi.server.UID;

class LoanARController extends CRUDController
{
    @Binding
    def binding;
    
    String serviceName = "LoanARService";
    String entityName = "loanar";
    String role = 'ACCT_ASSISTANT';
    
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = false;
        
    def selectedItem, selectedLiquidation, pettycashthreshold;
    def previtems;
    
    Map createEntity() {
        return [
            objid       : 'LAR' + new UID(),
            txnstate    : 'DRAFT',
            check       : [:],
            totalamount : 0
        ];
    }
    
    void afterCreate( data ) {
        //resolvePettyCashThreshold();
        itemHandler?.reload();
    }
    
    void afterSave(data) {
        checkEditable(data);
        //resolvePettyCashThreshold();
    }
    
    void afterOpen( data ) {
        checkEditable(data);
        //resolvePettyCashThreshold();
    }
    
    /*
    void resolvePettyCashThreshold() {
        def t = service.getPettyCashThreshold();
        pettycashthreshold = (t? t : 0);
    }
    */
    
    void checkEditable( data ) {
        if (data.txnstate != 'DRAFT') {
            allowEdit = false;
        } else if (data.txnstate == 'DRAFT') {
            allowEdit = true;
        }
        binding?.refresh('formActions');
    }
    
    def getOptionList() {
        return service.getOptionList();
    }
    
    public boolean isNavButtonVisible() { return false; }
    
    boolean getAllowInputCheck() {
        def flag = ClientContext.currentContext.headers.ROLES.containsKey("TREASURY.ACCT_ASSISTANT");
        println 'allow check input ' + flag;
        return flag;
    }
    
    void afterEdit( data ) {
        previtems = [];
        def item;
        data.items.each{ o->
            item = [:];
            item.putAll(o);
            previtems.add(item);
        }
    }
    
    void afterCancel() {
        entity.items = [];
        entity.items.addAll(previtems);
        def a = entity.items.amount.sum();
        entity.totalamount = (a? a : 0);
        itemHandler?.reload();
    }
    
    def itemHandler = [
        fetchList: { o->
            if (!entity.items) entity.items = [];
            return entity.items;
        },
        onOpenItem: { itm, colName->
            def handler = { o->
                selectedItem.putAll(o);
                //def item = entity.items.find{ it.objid==o.objid }
                //if (item) {
                //    println 'item ' + item;
                //    item.putAll(o);
                //}
                def a = entity.items.amount.sum();
                //println 'total ' + a;
                entity.totalamount = (a? a : 0);
                itemHandler?.reload();
                binding?.refresh('entity.(check.*|totalamount)');
            }
            return Inv.lookupOpener('loanaritem:open', [entity: itm, handler: handler, mode: mode]);
        }
    ] as BasicListModel;
    
    def addItem() {
        def handler = { o->
            if (!o.parentid) o.parentid = entity.objid;
            
            if (!entity._added) entity._added = [];
            entity._added.add(o);
            
            if (!entity.items) entity.items = [];
            entity.items.add(o);
            
            def a = entity.items.amount.sum();
            entity.totalamount = (a? a : 0);
            itemHandler?.reload();
            binding?.refresh('entity.(check.*|totalamount)');
        }
        return Inv.lookupOpener('loanaritem:create', [handler: handler, mode: mode]);
    }
    
    void removeItem() {
        if (!MsgBox.confirm("You are about to remove this item. Continue?")) return;
        
        if (!entity._removed) entity._removed = [];
        entity._removed.add(selectedItem);
        
        if (entity._added) entity._added.remove(selectedItem);
        
        entity.items.remove(selectedItem);
        
        def a = entity.items.amount.sum();
        entity.totalamount = (a? a : 0);
        itemHandler?.reload();
        binding?.refresh('entity.(check.*|totalamount)');
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
        if (!MsgBox.confirm("You are about to disapprove this document. Continue?")) return;
        
        entity = service.disapprove(entity);
    }
    
    void submitForLiquidation() {
        if (!MsgBox.confirm("You are about to submit this document for liquidation. Continue?")) return;
        
        entity = service.submitForLiquidation(entity);
    }
}

