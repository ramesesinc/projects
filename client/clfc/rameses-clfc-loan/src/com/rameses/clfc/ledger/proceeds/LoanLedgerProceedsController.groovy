package com.rameses.clfc.ledger.proceeds

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;
import java.rmi.server.UID;

class LoanLedgerProceedsController extends CRUDController
{
    @Caller
    def caller;
    
    @Service("DateService")
    def dateSvc;
    
    String serviceName = "LoanLedgerProceedsService";
    String entityName = "ledgerproceed";
    
    Map createPermission = [domain: 'LOAN', role: 'CAO_OFFICER'];
    
    boolean allowEdit = true;
    boolean allowDelete = true;
    boolean allowApprove = false;
    
    def borrowerLookupHandler = Inv.lookupOpener('ledgerborrower:lookup', [
        onselect: { o->
            //entity.ledger = o;
            entity.borrower = o.borrower;
            entity.parentid = o.objid;
        },
        pastdueledgers: true
    ]);
    def loadingOpener = Inv.lookupOpener("popup:loading", [:]);
    
    Map createEntity() {
        return [
            objid   : 'LPROC' + new UID(),
            txnstate: 'DRAFT',
            txntype : 'ONLINE'
        ];
    }
    
    void afterOpen( data ) {
        if (data.txnstate != 'DRAFT') {
            allowEdit = false;
            allowDelete = false;
            if (!data.txndate) data.txndate = dateSvc.getServerDateAsString().split(" ")[0];
        }
    }
    
    void submitForSelling() {
        if (!MsgBox.confirm("You are about to submit this proceed for selling. Continue?")) return;
        
        entity = service.submitForSelling(entity);
        allowEdit = false;
        allowDelete = false;
        caller?.reload();
    }
    
    /*
    def sell() {
        if (!MsgBox.confirm("You are about to sell this proceed. Continue?")) return;
        
        def handler = [
            onMessage: { o->
                //println 'onMessage '  + o;
                //println 'EOF ' + AsyncHandler.EOF;
                if (o == AsyncHandler.EOF) {
                    loadingOpener.handle.binding.fireNavigation("_close");
                    return;
                }                
                
                loadingOpener.handle.binding.fireNavigation("_close");
                entity = o;
                allowEdit = false;
                allowDelete = false;
                binding?.refresh();
                MsgBox.alert("Successfully sold proceed!");
            },
            onError: { o->
                loadingOpener.handle.binding.fireNavigation("_close");
                MsgBox.err(o);
            }
        ] as AsyncHandler;
        service.sold(entity, handler);
        return loadingOpener;
    }
    */
   
    void sell() {        
        if (!MsgBox.confirm("You are about to sell this proceed. Continue?")) return;
        
        entity = service.sold(entity);
        binding?.refresh();
        MsgBox.alert("Successfully sold proceed!");
    }
}

