package com.rameses.gov.etracs.rptis.landtax.province.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;

class RPTLedgerSyncModel 
{
    @Binding 
    def binding;
    
    @Caller
    def caller;
    
    @Service('ProvinceRPTLedgerService')
    def service
    
    def entity;
    
    String title = 'Synchronize Municipal Ledger';
    
    def oncomplete = {
        caller?.reload();
        binding.fireNavigation('_close');
    }
    
    def onerror = {e ->
        if (e.message.matches('.*Timeout.*')) {
            MsgBox.alert('Remote server is currently not available. Please try again later.');
        } else {
            MsgBox.alert(e.message);
        }
        binding.fireNavigation('_close');
    }
    
    def process = [
        run : {
            try{
                def params = [:]
                params.objid = entity.objid
                params.faasid = entity.faasid 
                params.tdno = entity.tdno
                params.barangayid = entity.barangayid 
                service.syncLedger(params)
                oncomplete();
            }
            catch(e){
                onerror(e);
            }
        }
    ] as Runnable;    
    
    
    def sync(){
        if (!entity) {
            entity = caller?.entityContext;
        }
        
        if (MsgBox.confirm('Sync with municipality ledger?')){
            new Thread(process).start();
            return 'default';
        }
        return null;
    }
    
    
}

