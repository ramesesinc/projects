package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

class RPTLedgerSubLedgerModel
{
    @Binding
    def binding;
    
    @Service('RPTLedgerService')
    def svc; 
    
    def entity;
    def selectedItem;
    def subledgers;
    
    void init(){
        subledgers = svc.getSubLedgers([objid:entity.objid]);
        calcTotals();
    }
    def listHandler = [
        fetchList :{ subledgers }
    ] as BasicListModel
    
    
    void refresh(){
        init();
        listHandler.reload();
    }    
    
         
    def selectedSubLedger;
    def totalSubledgerSqm = 0.0;
    def totalSubledgerMV = 0.0;
    def totalSubledgerAV = 0.0;
    def subledgerCount = 0;
    
    def addSubLedger(){
        if (totalSubledgerSqm >= entity.totalareasqm)
            throw new Exception('Subledger is no longer allowed.\nMain Ledger area has totally been allocated.')
            
        return InvokerUtil.lookupOpener('rptsubledger:create', [
                ledger            : entity,
                totalSubledgerArea : totalSubledgerSqm, 
                totalSubledgerMV   : totalSubledgerMV, 
                totalSubledgerAV   : totalSubledgerAV, 
                
                onadd  : {
                    subledgers << it;
                    listHandler.load();
                    calcTotals();
                }
        ])
    }
    
    def openSubLedger(){
        return InvokerUtil.lookupOpener('rptsubledger:open', [
                ledger              : entity,
                totalSubledgerArea   : totalSubledgerSqm, 
                totalSubledgerMV     : totalSubledgerMV, 
                totalSubledgerAV     : totalSubledgerAV, 
                entity               : selectedItem,
                //entity              : svc.openLedger(selectedSubLedger),
                onupdate  : {
                    selectedItem.putAll(it)
                    listHandler.load();
                    calcTotals();
                }
        ])
    }
    
    def deleteSubLedger(){
        if (MsgBox.confirm('Delete selected item?')){
            def item = subledgers.find{it.objid == selectedItem.objid}
            svc.deleteSubLedger(item);
            subledgers.remove(item);
            listHandler.load();
            calcTotals();
        }
    }
    
    void calcTotals(){
        totalSubledgerSqm = 0.0;
        totalSubledgerMV = 0.0;
        totalSubledgerAV = 0.0;
        subledgerCount = 0;
        if (entity.totalareaha == null){
            entity.totalareaha = entity.totalareasqm /10000;
        }
        
        if (subledgers) {
            totalSubledgerSqm = subledgers.totalareasqm.sum();
            totalSubledgerMV = subledgers.totalmv.sum();
            totalSubledgerAV = subledgers.totalav.sum();
            subledgerCount = subledgers.size();
        }
        binding?.refresh('totalSubledger.*|subledgerCount');
    }
        
}