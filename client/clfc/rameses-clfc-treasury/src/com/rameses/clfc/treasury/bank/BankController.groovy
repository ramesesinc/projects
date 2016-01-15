package com.rameses.clfc.treasury.bank;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class BankController extends CRUDController
{
    @Caller
    def caller;
    
    String serviceName = 'BankService';
    String entityName = 'bank';

    String createFocusComponent = 'entity.code';
    String editFocusComponent = 'entity.name';             
    boolean showConfirmOnSave = false;
    boolean allowApprove = false;
    boolean allowDelete = false;

    Map createPermission = [domain:'TREASURY', role:'CASHIER,ACCT_ASSISTANT', permission:'bank.create']; 
    Map editPermission = [domain:'TREASURY', role:'CASHIER,ACCT_ASSISTANT', permission:'bank.edit']; 

    void beforeSave(data) {
        if (mode == 'create') { 
            data.objid = data.code; 
        } 
    }
    
    void activate() {
        if (MsgBox.confirm('You are about to activate this record. Continue?')) {
            entity = service.activate([objid: entity.objid]); 
            EventQueue.invokeLater({ caller?.reload() }); 
        } 
    } 
    
    void deactivate() {
        if (MsgBox.confirm('You are about to deactivate this record. Continue?')) {
            entity = service.deactivate([objid: entity.objid]); 
            EventQueue.invokeLater({ caller?.reload() }); 
        } 
    } 
    
    def viewLogs() {
        return Inv.lookupOpener('txnlog:open', [query: [txnid: entity.objid]]); 
    } 
}
