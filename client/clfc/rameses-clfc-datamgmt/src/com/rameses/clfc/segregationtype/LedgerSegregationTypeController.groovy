package com.rameses.clfc.segregationtype;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class LedgerSegregationTypeController extends CRUDController
{
    String serviceName = "LoanSegregationTypeService";
    String entityName = "segregation_type";

    boolean showConfirmOnSave = false;
    boolean allowApprove = false;
    boolean allowDelete = false;

    Map createEntity() { 
        return [objid: 'SEG'+new java.rmi.server.UID()]; 
    } 
    
    def category;
    def getCategoryList() {
        return service.getCategories([:]);
    }
    
    void afterOpen( data ) {
        category = categoryList.find{ it.value == data.category }
    }
    
    void activate() {
        if (MsgBox.confirm('You are about to activate this record. Continue?')) {
            entity = service.changeState([objid: entity.objid, state:'ACTIVATED']); 
            EventQueue.invokeLater({ caller?.reload() }); 
        } 
    } 
    
    void deactivate() {
        if (MsgBox.confirm('You are about to deactivate this record. Continue?')) {
            entity = service.changeState([objid: entity.objid, state:'DEACTIVATED']); 
            EventQueue.invokeLater({ caller?.reload() }); 
        } 
    } 
    
    void beforeSave( data ) {
        data.category = category?.value;
    }
    
    def viewLogs() {
        return Inv.lookupOpener('txnlog:open', [query: [txnid: entity.objid]]); 
    }   
}