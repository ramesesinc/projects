package com.rameses.gov.etracs.rptis.landtax.province.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class RPTLedgerModel
{
    @Binding 
    def binding;
    
    @Caller
    def caller;
    
    @Service('ProvinceRPTLedgerService')
    def svc;
    
    String title = 'Create Province Ledger';
    
    def mode; 
    def msg; 
    def faas;
    def entity;
    
    void init() {
        mode = 'init';
    }
    
    def onComplete = {
        msg = 'Loading newly created ledger...';
        binding.refresh('msg');
        caller.refresh();
        
        def inv = Inv.lookupOpener('rptledger:open', [entity:entity]);
        binding.fireNavigation(inv);
        
        faas = null;
        mode = 'init';
        binding.refresh();
    }
    
    def onError = {err ->
        mode = 'ERROR';
        binding.refresh();
        MsgBox.alert(err);
    }
    
    
    def createLedger = {
        mode = 'processing';
        msg = 'Creating new ledger. Please wait...';
        binding.refresh('faas|msg');
        entity = svc.createLedger(faas);
    }
    
    def syncLedger = {
        msg = 'Synchronizing with municipal ledger. Please wait...';
        binding.refresh('msg');
        
        try {
            def params = [:]
            params.objid = entity.objid
            params.faasid = entity.faasid 
            params.tdno = entity.tdno
            params.barangayid = entity.barangayid 
            svc.syncLedger(params)
        } catch(e) {
            msg = null;
            println 'SyncLedger [ERROR] ' + e.message;
            binding.refresh('msg');
        }
    }
    
    def task = [
        run :  {
            try {
                createLedger();
                syncLedger();
                onComplete();
            } catch(e) {
                onError(e.message);
            }
        }
    ] as Runnable 
    
    def create(){
        if (MsgBox.confirm("Create ledger?")){
            new Thread(task).start();
        }
        return null;
    }
    
    def getLookupFaas(){
        return Inv.lookupOpener('faas:lookup', [
            onselect : {
                if (!'CURRENT'.equalsIgnoreCase(it.state))
                    throw new Exception('FAAS is not current.')
                faas = it;
            },
            
            onempty : {
                faas = null;
            }
        ])
    }
    
    
    public def getSelectedItem(){
        return entity;
    }
    
}