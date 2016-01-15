package com.rameses.clfc.treasury.overage.voidrequest

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class OverageVoidRequestController extends CRUDController 
{    
    @Caller
    def caller;
    
    @Binding
    def binding;    
    
    String serviceName = "OverageVoidRequestService";
    String entityName = "overagevoidrequest";
    
    boolean allowDelete = false;
    boolean allowApprove = false;
    boolean allowEdit = true;
    
    Map createPermission = [domain: 'TREASURY', role: 'CASHIER'];
    Map editPermission = [domain: 'TREASURY', role: 'CASHIER'];
    
    def overageLookupHandler = Inv.lookupOpener('overage:lookup', [
         onselect: { o->
             if (!o.collectorname) o.collectorname = o.collector.name;
             entity.overage = o;
             binding.refresh("entity.overage.*");
         },
         state: 'NOTED'
    ]);
    
    void afterOpen( data ) {
        if (data.txnstate!='DRAFT') {
            allowEdit = false;
            binding?.refresh('formActions');
        }
    }
    
    Map createEntity() {
        return [
            objid   : 'OVR' + new UID(),
            txnstate: 'DRAFT',
            overage : [:]
        ];
    }
    
    void submitForApproval() {
        if (!MsgBox.confirm("You are about to submit this document for approval. Continue?")) return;
        
        entity = service.submitForApproval(entity);
        allowEdit = false;
        EventQueue.invokeLater({
             caller?.reload();
             binding?.refresh();
        });
    }
    
    void approveDocument() {
        if (!MsgBox.confirm("You are about to approve this document. Continue?")) return;
        
        entity = service.approveDocument(entity);
        EventQueue.invokeLater({
             caller?.reload();
             binding?.refresh();
        });
    }
    
    void disapprove() {
        if (!MsgBox.confirm("You are about to disapprove this document. Continue?")) return;
        
        entity = service.disapprove(entity);
        EventQueue.invokeLater({
             caller?.reload();
             binding?.refresh();
        });
    }
}

