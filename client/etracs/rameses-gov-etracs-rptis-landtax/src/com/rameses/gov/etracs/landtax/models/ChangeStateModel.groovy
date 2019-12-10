package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*


public class ChangeStateModel 
{
    @Caller
    def caller;
    
    @Binding
    def binding
    
    @Service('RPTLedgerService')
    def svc 
    
    def entity 
    
    String title = 'Change Ledger State'
    
    def currentstate;
    
    def states = ['PENDING', 'APPROVED', 'CANCELLED'];
    
    
    void init(){
        currentstate = entity.state;
        states.remove(currentstate);
    }
    
    
    def update(){
        if (MsgBox.confirm('Update ledger state?')){
            svc.updateState(entity);
            caller.reloadEntity();
            caller.refreshSections();
            return '_close'
        }
    }
    
}

