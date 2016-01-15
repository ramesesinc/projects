package com.rameses.clfc.treasury.teller;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class TellerController extends CRUDController
{
    @Caller
    def caller;

    @Binding
    def binding;

    String serviceName = "TellerService";
    String entityName = "teller";

    String createFocusComponent = 'entity.lastname'; 
    String editFocusComponent = 'entity.lastname';

    boolean showConfirmOnSave = false;
    boolean allowApprove = false;
    boolean allowDelete = false;
    
    boolean isAllowEdit() {
        if (entity?.txnstate == 'CLOSED') return false; 
        
        return super.isAllowEdit(); 
    }

    Map createPermission = [domain:'TREASURY', role:'CASHIER', permission:'passbook.create']; 
    Map editPermission = [domain:'TREASURY', role:'CASHIER', permission:'passbook.edit']; 

    Map createEntity() {
        return [objid: 'TLR' + new UID(), mode: 'ONLINE'];
    }

    void activate() {
        if (MsgBox.confirm('You are about to activate this teller. Continue?')) {
            entity = service.activate([objid: entity.objid]); 
            EventQueue.invokeLater({ caller.reload() }); 
        } 
    } 
    
    void deactivate() {
        if (MsgBox.confirm('You are about to deactivate this teller. Continue?')) {
            entity = service.deactivate([objid: entity.objid]); 
            EventQueue.invokeLater({ caller.reload() }); 
        } 
    }     

    def viewLogs() {
        return Inv.lookupOpener('txnlog:open', [query: [txnid: entity.objid]]); 
    } 
}