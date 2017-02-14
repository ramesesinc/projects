package com.rameses.gov.etracs.rptis.landtax.province.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class UnpostedLedgerModel
{
    @Binding 
    def binding;
    
    @Caller
    def caller;
    
    @Service('ProvinceUnpostedLedgerService')
    def svc;
    
    def faas;
    String title = 'Create Unposted Ledger';
    
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
    
    def create(){
        if (MsgBox.confirm("Create ledger?")){
            def ledger = svc.createLedger(faas)
            faas = null
            binding.refresh('.*');
            return Inv.lookupOpener('rptledger:open', [entity:ledger]);
        }
    }
    
}