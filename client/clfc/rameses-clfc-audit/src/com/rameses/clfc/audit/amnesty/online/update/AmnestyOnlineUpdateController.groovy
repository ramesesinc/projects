package com.rameses.clfc.audit.amnesty.online.update

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class AmnestyOnlineUpdateController extends CRUDController
{
    @Binding
    def binding;
    
    @PropertyChangeListener
    def listener = [
        "entity.update.isspotcash": { o->
            binding?.refresh('entity.update.*');
        }
    ]
    
    String serviceName = 'AmnestyOnlineUpdateService';
    String entityName = 'amnestyonlineupdate';
    
    Map createEntity() {
        return [
            objid   : 'AOU' + new UID(),
            txnstate: 'DRAFT'
        ];
    }
    
    boolean allowDelete = false;
    boolean allowApprove = false;
    boolean allowEdit = true;
    
    def amnestyLookup = Inv.lookupOpener('amnestyonline:lookup', [
         onselect: { o->
             //println 'amnesty ' + o;
             entity.amnesty = o;
             if (!entity.update) entity.update = [:];
             entity.update.putAll(o.grantedoffer);
             entity.update.dtstarted = o.dtstarted;
             binding?.refresh();
         }
    ]);
    
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
    
    void submitForApproval() {
        if (!MsgBox.confirm("You are about to submit this document for approval. Continue?")) return;
        
        entity = service.submitForApproval(entity);
        checkEditable(entity);
    }
    
    void approveDocument() {
        if (!MsgBox.confirm("You are about to approve this document. Continue?")) return;
        
        entity = service.approveDocument(entity);
        checkEditable(entity);
    }
    
    void disapprove() {
        if (!MsgBox.confirm("You are about to disapprove this document. Continue?")) return;
        
        entity = service.disapprove(entity);
        checkEditable(entity);
    }
    
}

