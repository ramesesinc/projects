package com.rameses.clfc.treasury.ledger.amnesty.fix

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class AvailFixAmnestyController extends CRUDController
{
    @Binding
    def binding;
    
    def amnestytype = 'FIX';
    
    String serviceName = 'AvailFixAmnestyService';
    String entityName = 'availfixamnesty';
    
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = true;
    
    def selectedOption, option, amnesty;
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
             }
             binding?.refresh();
         },
         type       : amnestytype,
         foravail   : true
    ]);
    
    Map createEntity() {
        return [
            objid   : 'AF' + new UID(),
            txnstate: 'DRAFT'
        ];
    }
    
    void afterCreate( data ) {
        option = null;
        listHandler?.reload();
    }
    
    void afterOpen( data ) {
        checkEditable(data);
        if (data.option) {
            option = data.option;
            binding?.refresh();
        }
    }
    
    void beforeSave( data ) {
        //if (!option) throw new Exception('Please specify an option to avail.');
        
        if (option) {
            data.option = option;
        }
    }
    
    void checkEditable( data ) {
        allowEdit = false;
        if (data.txnstate=='DRAFT') {
            allowEdit = true;
        }
        binding?.refresh('formActions');
    }
    
    void setSelectedOption( selectedOption ) {
        this.selectedOption = selectedOption;
        if (entity.txnstate == 'DRAFT') {
            option = selectedOption;
            binding?.refresh();
        }
    }
    
    def listHandler = [
        fetchList: { o->
            if (!entity.items) entity.items = [];
            return entity.items;
        }
    ] as BasicListModel;
    
    def addItem() {
        def handler = { o->
            def i = entity?.items?.find{ it.objid == o.objid }
            if (i) throw new Exception('This option has already been selected.');
        
            def item = service.getItemInfo(o);
            item.parentid = entity.objid;
            
            if (!entity._addeditems) entity._addeditems = [];
            entity._addeditems.add(item);
            
            if (!entity.items) entity.items = [];
            entity.items.add(item);
            listHandler?.reload();
        } 
        def params = [
            onselect    : handler,
            amnestyid   : entity?.amnestyid
        ];
        def op = Inv.lookupOpener('fixrecommendation:lookup', params);
        if (!op) return null;
        return op;
    }
    
    void removeItem() {
        if (!MsgBox.confirm('You are about to remove this item. Continue?')) return;
        
        
        if (entity._addeditems) entity._addeditems.remove(selectedOption);
        
        if (!entity._removeditems) entity._removeditems = [];
        entity._removeditems.add(selectedOption);
        
        entity.items?.remove(selectedOption);
        
        listHandler?.reload();
    }
    
    void avail() {
        if (!option) throw new Exception('Please specify option to avail.');
        
        def msg = '<html>You are about to avail option <b>' + option?.description + '</b>. Continue?</html>';
        if (!MsgBox.confirm(msg)) return;
        
        entity = service.avail(entity);
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
    
    void sendBack() {
        if (!MsgBox.confirm('You are about to send back this document. Continue?')) return;
        
        
    }
    
}

