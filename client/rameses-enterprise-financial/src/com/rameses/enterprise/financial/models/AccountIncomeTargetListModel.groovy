package com.rameses.enterprise.financial.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;

class AccountIncomeTargetListModel extends com.rameses.seti2.models.CrudListModel {
        
    @Service('DateService')
    def dateSvc; 
        
    void afterInit() { 
        query.year = dateSvc.getServerYear(); 
    } 
    
    void removeEntity() {
        if(!selectedItem) return;

        if (!isDeleteAllowed()) 
            throw new Exception("Delete is not allowed for this transaction");
        if( selectedItem.system != null && selectedItem.system == 1 )
            throw new Exception("Cannot remove system created file");

        try {
            beforeRemoveItem(); 
        } catch(BreakException be) { 
            return; 
        } 
        
        if( !MsgBox.confirm('You are about to delete this record. Proceed?')) return;

        def ename = (entitySchemaName ? entitySchemaName : schemaName);
        def m = [_schemaname : ename, findBy: [objid: selectedItem.objid]];
        getPersistenceService().removeEntity( m );
        listHandler.reload();
    }  
}