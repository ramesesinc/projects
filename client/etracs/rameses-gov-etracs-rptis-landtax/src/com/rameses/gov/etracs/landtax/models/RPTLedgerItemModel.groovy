package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;

class RPTLedgerItemModel
{
 
    @Caller
    def caller;
    
    @Binding
    def binding;
    
    def svc;
    def entity;
    def item;
    def faases;
    def mode;
    
    def onadd;
    def onupdate;
    def quarters = [1,2,3,4]
    
    def MODE_CREATE = 'create';
    def MODE_EDIT = 'edit';
    
    String title = 'Ledger Entry Information'
    
    @PropertyChangeListener
    def listener = [
        'item.paidqtr' : {
            item.fullypaid = (item.paidqtr == 4 ? 1 : 0)
            binding.refresh('item.fullypaid');
        }
    ]
    
    void create(){
        item = [
            objid       : 'LI' + new java.rmi.server.UID(),
            rptledgerid : entity.objid, 
            av          : 0.0,
            taxdifference : 0,
            paidqtr       : null,
            fullypaid     : 0,
        ]
        mode = MODE_CREATE;
    }
    
    void edit(){
        mode = MODE_EDIT;
    }
    
    def add(){
        if (MsgBox.confirm('Add Item?')){
            def ledgerfaas = faases.find{ item.year >= it.fromyear && (item.year <= it.toyear || it.toyear == 0 )}
            if (!ledgerfaas)
                throw new Exception('Year does not match a FAAS history range.')
            
            item.rptledgerfaasid = ledgerfaas.objid;
            item.ledgerfaas = ledgerfaas;
            item.isnew = true;
            resolvePaidQtr();
            svc.saveLedgerItem(item);
            if (onadd) onadd(item);
            return '_close';
        }
    }
    
    def update(){
        if (MsgBox.confirm('Update Item?')){
            def ledgerfaas = faases.find{ item.year >= it.fromyear && (item.year <= it.toyear || it.toyear == 0 )}
            if (!ledgerfaas)
                throw new Exception('Year does not match a FAAS history range.')
            
            item.rptledgerfaasid = ledgerfaas.objid;
            item.ledgerfaas = ledgerfaas;
            item.isnew = false;
            resolvePaidQtr();
            svc.saveLedgerItem(item);
            if (onupdate) onupdate(item);
            return '_close';
        }
    }
    
    void resolvePaidQtr(){
        item.paidqtr = (item.paidqtr == null ? 0 : item.paidqtr);
    }
    
    
}