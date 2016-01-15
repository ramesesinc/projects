package com.rameses.clfc.treasury.passbook;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class PassbookController extends CRUDController
{
    @Caller
    def caller;
    
    String serviceName = 'PassbookService';
    String entityName = 'passbook';

    String createFocusComponent = 'entity.passbookno'; 
    String editFocusComponent = 'entity.passbookno';
    String prefixId = 'PSBK';
    boolean showConfirmOnSave = false;
    boolean allowApprove = false;
    boolean allowDelete = false;
    
    boolean isAllowEdit() {
        if (entity?.txnstate == 'CLOSED') return false; 
        
        return super.isAllowEdit(); 
    }

    Map createPermission = [domain:'TREASURY', role:'CASHIER', permission:'passbook.create']; 
    Map editPermission = [domain:'TREASURY', role:'CASHIER', permission:'passbook.edit']; 

    void activate() {
        if (MsgBox.confirm('You are about to activate this account. Continue?')) {
            entity = service.activate([objid: entity.objid]); 
            EventQueue.invokeLater({ caller.reload() }); 
        } 
    } 
    
    void deactivate() {
        if (MsgBox.confirm('You are about to deactivate this account. Continue?')) {
            entity = service.deactivate([objid: entity.objid]); 
            EventQueue.invokeLater({ caller.reload() }); 
        } 
    } 
    
    def closeAccount() {
        if (!MsgBox.confirm('You are about to close this account. Continue?')) return;
        
        def handler = { remarks-> 
            try {
                entity = service.closeAccount([objid: entity.objid, remarks:remarks]); 
                EventQueue.invokeLater({ caller?.reload() }); 
                binding.refresh();
            } catch (Throwable t) {
                MsgBox.err(t.message);
            }
        } 
        return Inv.lookupOpener('remarks:create', [title: 'Remarks for closing account', handler: handler]);
    }
    
    def viewLogs() {
        return Inv.lookupOpener('txnlog:open', [query: [txnid: entity.objid]]); 
    } 
    
    def bankLookup = Inv.lookupOpener('bank:lookup', [
        onselect: {o-> 
            entity.bank = o;
            binding.refresh(); 
        } 
    ]);     
}
