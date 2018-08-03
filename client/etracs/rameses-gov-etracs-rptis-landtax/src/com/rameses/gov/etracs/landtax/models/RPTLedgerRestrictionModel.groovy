package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

class RPTLedgerRestrictionModel
{
    @Service('RPTLedgerService')
    def svc; 
    
    def entity;
    def selectedItem;
    def items;
    def type = 'ledger'
    
    void init(){
        items = svc.getRestrictions([objid:entity.objid]);
    }
    
    void refresh(){
        init();
        listHandler.reload();
    }
    
    def listHandler = [
        fetchList :{ items }
    ] as BasicListModel
    
    
    
    
    def oncreate = {
        items << it 
        listHandler.reload()
    }    
    
    def onupdate = {
        selectedItem.putAll(it)
        listHandler.refresh()
    }
    
    def getRestrictionParam(){
        def p = [oncreate:oncreate, onupdate:onupdate]
        p.parent = [objid:entity.faasid, tdno:entity.tdno, fullpin:entity.fullpin, owner:entity.owner]
        p.ledger = [objid:entity.objid]
        return p
    }
    
    def add(){
        return Inv.lookupOpener('faas_restriction:create', restrictionParam)
    }
    
    def open(){
        def p = restrictionParam
        p.entity = selectedItem
        p.entity._type = type;
        return Inv.lookupOpener('faas_restriction:open', p)
    }    
    
    void delete(){
        if (!selectedItem) return;
        if (MsgBox.confirm('Delete restriction?')){
            svc.removeRestriction(selectedItem);
            items.remove(selectedItem);
            listHandler.reload();
        }
    }

}