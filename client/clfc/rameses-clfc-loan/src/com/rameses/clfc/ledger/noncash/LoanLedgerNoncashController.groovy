package com.rameses.clfc.ledger.noncash

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class LoanLedgerNoncashController extends CRUDController
{
    @Binding
    def binding;
    
    String serviceName = "LoanLedgerNoncashService";
    String entityName = 'ledgernoncash';
    
    boolean allowDelete = false;
    boolean allowApprove = false;
    boolean allowEdit = true;
    
    Map createPermission = [domain: 'LOAN', role: 'CASHIER'];
    Map editPermission = [domain: 'LOAN', role: 'CASHIER'];
    
    def borrowerLookupHandler = Inv.lookupOpener('ledgerborrower:lookup', [
        onselect: { o->
            //entity.ledger = o;
            entity.borrower = o.borrower;
            entity.parentid = o.objid;
        }
    ]);
    
    Map createEntity() {
        allowEdit = true;
        return  [
            objid   : 'LNC' + new UID(),
            txntype : 'ONLINE',
            txnstate: 'DRAFT'
        ];
    }
    
    void afterOpen( data ) {
        checkEditable(data);
    }
    
    private void checkEditable( data ) {
        allowEdit = false;
        if (data.txnstate == 'DRAFT') {
            allowEdit = true;
        }
        binding?.refresh('formActions');
    }
    
    void collect() {
        if (!MsgBox.confirm("You are about to collect this non-cash. Continue?")) return;
        
        entity = service.collect(entity);
        checkEditable(entity);
    }
}

