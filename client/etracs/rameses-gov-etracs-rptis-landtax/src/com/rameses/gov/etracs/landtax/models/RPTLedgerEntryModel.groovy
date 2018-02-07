package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

class RPTLedgerEntryModel
{
    @Service('RPTLedgerService')
    def svc; 
    
    def entity;
    def selectedItem;
    def entries;
    def faases;
    
    void init(){
        def ledger = [objid:entity.objid]
        entries = svc.getLedgerItems(ledger);
        faases = svc.getTaxableLedgerFaases(ledger);
    }
    
    void refresh(){
        init();
        listHandler.reload();
    }
    
    def listHandler = [
        fetchList :{ entries }
    ] as BasicListModel
    
    def getAmountDue(){
        if (entries)
            return entries.total.sum();
        return 0.0
    }
    
    
    def addLedgerItem(){
        return InvokerUtil.lookupOpener('rptledgeritem:create', [
            svc         : svc,
            entity      : entity, 
            faases      : faases,
            onadd       : { refresh() }
        ] )
    }
    
    def editLedgerItem(){
        return InvokerUtil.lookupOpener('rptledgeritem:edit', [
            svc         : svc,
            entity      : entity, 
            item        : selectedItem,
            faases      : faases,
            
            onupdate : { refresh() }
        ] )
    }
       
    void deleteLedgerItem(){
        if (!selectedItem) return;
        if (selectedItem.taxdifference == false){
            throw new Exception('Only Tax Difference item can be deleted.');
        }
        if (MsgBox.confirm('Delete selected item?')){
            svc.removeLedgerItem(selectedItem);
            refresh();
        }
    }
}