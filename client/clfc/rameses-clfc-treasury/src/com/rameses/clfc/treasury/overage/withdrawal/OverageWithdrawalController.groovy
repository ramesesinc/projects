package com.rameses.clfc.treasury.overage.withdrawal;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;
import java.rmi.server.UID;

class OverageWithdrawalController extends CRUDController
{
    @Binding
    def binding;

    String serviceName = 'OverageWithdrawalService';
    String entityName = 'overagewithdrawal';

    boolean allowEdit = true;
    boolean allowDelete = false;
    boolean allowApprove = false;
    
    Map createPermission = [domain: 'TREASURY', role: 'ACCT_ASSISTANT'];
    Map editPermission = [domain: 'TREASURY', role: 'ACCT_ASSISTANT'];

    @Service("DateService")
    def dateSvc;

    def overage, borrower;
    def loadingOpener = Inv.lookupOpener("popup:loading", [:]);

    def overageLookup = Inv.lookupOpener('overagewithinfo:lookup', [
        onselect: { o->
            if (o.balance == 0) throw new Exception("Overage selected has no balance remaining.");

            entity.overage = o;
            binding.refresh('overage|entity.overage.*');
        }, 
        state: 'APPROVED', 
        withbalance: true
    ]);

    def borrowerLookup = Inv.lookupOpener('ledgerborrower:lookup', [
        onselect: { o->
            entity.borrower = o.borrower;
            entity.loanapp = o.loanapp;
            binding.refresh('borrower|entity.borrower.*');
        }
    ])

    def withdrawaltype;
    def options = [
        [value: 'Payment', name: 'payment'],
        [value: 'Return', name: 'return']
    ];

    Map createEntity() {
        return [
            objid   : 'OFD' + new UID(),
            txnstate: 'DRAFT',
            txndate : dateSvc.getServerDateAsString().split(" ")[0]
        ];
    }

    void afterOpen( data ) {
        checkEditable(data);
        //if (data.txnstate != 'DRAFT') allowEdit = false;
        withdrawaltype = entity.txntype;
    }
    
    void checkEditable( data ) {
        allowEdit = false;
        if (data.txnstate == 'DRAFT') {
            allowEdit = true;
        }
        binding?.refresh('formActions');
    }

    void beforeSave( data ) {
        if (entity.amount > entity.overage.balance)
            throw new Exception("Insufficient overage balance for amount specified."); 
        
        entity.txntype = withdrawaltype;
    } 

    void submitForApproval() {
        if (!MsgBox.confirm("You are about to submit this document for approval. Continue?")) return;

        entity = service.submitForApproval(entity);
        checkEditable(entity);
    }

    def approveDocument() {
        if (!MsgBox.confirm("You are about to approve this document. Continue?")) return;

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
                checkEditable(entity);
                binding.refresh();
                MsgBox.alert("Successfully rebuilt collection sheet!");
            },
            onError: { o->
                loadingOpener.handle.binding.fireNavigation("_close");
                MsgBox.err(o);
                /*if (o instanceof java.util.concurrent.TimeoutException) {
                
                } else {
                    throw new Exception(o.message);
                }*/
            }
        ] as AsyncHandler;
        service.approveDocument(entity, handler);
        return loadingOpener;
    }

    void disapprove() {
        if (!MsgBox.confirm("You are about to disapprove this document. Continue?")) return;

        entity = service.disapprove(entity);
        checkEditable(entity);
    }
    
}