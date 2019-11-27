package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class RPTLedgerRedFlagModel extends com.rameses.seti2.models.CrudFormModel {
    @Service('RPTLedgerRedFlagService')
    def svc;
    
    boolean fromLedger = false;
    
    def getLookupLedger() {
        return Inv.lookupOpener('rptledger:lookup', [
            onselect : {
                if (it.state == 'PENDING') throw new Exception('Pending ledger is not allowed.');
                if (it.state == 'CANCELLED') throw new Exception('Cancelled ledger is not allowed.');
                entity.rptledger = it;
            },
           onempty: {
               entity.rptledger = null;
           }
        ]);
    }
    
    void approve() {
        if (MsgBox.confirm('Approve?')) {
            entity.putAll(svc.approve(entity));
            reload();
        }
    }
    
    def resolve() {
        def onresolve = {remarks ->
            entity.remarks = remarks;
            svc.resolve(entity);
            reload();
        }
        return Inv.lookupOpener('rptledger_redflag:resolve', [onresolve: onresolve]);
    }
    
    def getRedFlagTypes() {
        return svc.getRedFlagTypes();
    }
    
    def getBlockActions() {
        return svc.getBlockActions();
    }
}