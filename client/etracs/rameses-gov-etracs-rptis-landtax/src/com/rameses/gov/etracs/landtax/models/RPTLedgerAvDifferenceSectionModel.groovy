package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

class RPTLedgerAvDifferenceSectionModel
{
    @Caller
    def caller;
    
    @Service('RPTLedgerService')
    def svc; 
    
    def entity;
    def selectedItem;
    def differences;
    def ledger
    
    void init(){
        ledger = [objid:entity.objid]
        differences = svc.getAvDifferences(ledger);
    }
    
    void refresh(){
        init();
        listHandler?.reload();
    }
    
    def listHandler = [
        fetchList :{ differences }
    ] as BasicListModel
    
    def addItem(){
        return InvokerUtil.lookupOpener('rptledger_avdifference:create', [
            svc         : svc,
            entity      : entity, 
            onadd       : {
                refresh();
                caller?.refreshSections();
            },
        ] )
    }
        
    void deleteItem(){
        if (!selectedItem) return;
        if (MsgBox.confirm('Delete selected item?')){
            svc.removeAvDifference(selectedItem);
            refresh();
            caller?.refreshSections();
        }
    }
}