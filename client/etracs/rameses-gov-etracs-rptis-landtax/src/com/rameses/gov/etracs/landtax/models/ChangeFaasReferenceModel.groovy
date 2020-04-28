package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*


public class ChangeLedgerFaasController 
{
    @Caller
    def caller;
    
    @Service('RPTLedgerService')
    def svc;
    
    
    @Binding
    def binding;
    
    String title = 'Change Ledger FAAS Reference'
    
    def entity;
    def newfaas;
    
    void init(){
    }
    
    def changeReference(){
        if (MsgBox.confirm('Post new Ledger FAAS reference?')){
            svc.updateFaasReference(entity, newfaas)
            caller.reloadEntity();
            caller.refreshSections();
            return '_close';
        } 
        return null;
    }
    
    def getLookupFaas(){
        return Inv.lookupOpener('faas:lookup', [
            onselect  : {
                validateFaas(it);
                newfaas = it;
            },
            onempty : {
                newfaas = null;
            },
        ])
    }
    
    void validateFaas(faas){
        if (faas.state != 'CURRENT')
            throw new Exception('FAAS is not yet current. Only current FAAS is allowed.');
        if (entity.faasid == faas.objid)
            throw new Exception('FAAS is the same with the Ledger reference.');
        if (entity.rputype != faas.rputype)
            throw new Exception('FAAS proeprty type is invalid. Only ' + entity.rputype + ' property type is allowed.');
    }
    
}

