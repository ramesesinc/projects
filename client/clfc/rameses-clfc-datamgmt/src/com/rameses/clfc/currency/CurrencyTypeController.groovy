package com.rameses.clfc.currency;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class CurrencyController extends CRUDController
{
    @Caller
    def caller;

    String serviceName = 'CurrencyTypeService';
    String entityName = 'currencytype';

    String createFocusComponent = 'entity.code';
    String editFocusComponent = 'entity.name';             
    boolean showConfirmOnSave = false;
    boolean allowApprove = false;
    boolean allowDelete = false;
    
    Map createPermission = [domain:'DATAMGMT', role:'DATAMGMT_AUTHOR', permission:'curencytype.create']; 
    Map editPermission = [domain:'DATAMGMT', role:'DATAMGMT_AUTHOR', permission:'curencytype.edit']; 

    Map createEntity() { 
        return [objid: 'CURRTYPE'+new UID()]; 
    } 
    
    void activate() {
        if (MsgBox.confirm('You are about to activate this record. Continue?')) {
            entity = service.changeState([objid: entity.objid, txnstate:'ACTIVATED']); 
            EventQueue.invokeLater({ caller?.reload() }); 
        } 
    } 
    
    void deactivate() {
        if (MsgBox.confirm('You are about to deactivate this record. Continue?')) {
            entity = service.changeState([objid: entity.objid, txnstate:'DEACTIVATED']); 
            EventQueue.invokeLater({ caller?.reload() }); 
        } 
    } 
    
    def viewLogs() {
        return Inv.lookupOpener('txnlog:open', [query: [txnid: entity.objid]]); 
    }    
}
