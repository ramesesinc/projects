package com.rameses.clfc.followup.result

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class FollowupResultController extends CRUDController
{
    @Binding
    def binding;
    
    @Service('DateService')
    def dateSvc;
    
    String serviceName = 'LoanLedgerFollowupResultService';
    String entityName = 'followupresult';
    
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = true;
    
    Map createEntity() {
        return [
            objid   : 'FR' + new UID(), 
            txnstate: 'DRAFT', 
            txndate : dateSvc.getServerDateAsString()
        ];
    }
    
    def getBorrowerLookup() {
        def handler = { o->
            def data = service.getLedgerInfo(o);
            if (data) {
                entity.putAll(data);
            }
            binding?.refresh();
        }
        return Inv.lookupOpener('followup-borrower:lookup', [onselect: handler, date: entity?.txndate]);
    }
    
    void afterOpen( data ) {
        checkEditable(data);
    }
    
    void checkEditable( data ) {
        allowEdit = false;
        if (data.txnstate == 'DRAFT') {
            allowEdit = true;
        }
        
        binding?.refresh('formActions');
    }
    
    void confirm() {
        if (!MsgBox.confirm('You are about to confirm this document. Continue?')) return;
        
        entity = service.confirm(entity);
        checkEditable(entity);
    }
}

