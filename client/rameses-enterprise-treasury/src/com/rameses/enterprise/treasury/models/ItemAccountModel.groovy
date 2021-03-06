package com.rameses.enterprise.treasury.models; 

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class ItemAccountModel extends com.rameses.seti2.models.CrudFormModel {
    
    boolean isAllowApprove() {
         return ( mode=='read' && entity.state.toString().matches('DRAFT|ACTIVE') ); 
    }
    boolean isAllowDisapprove() {
         return ( mode=='read' && entity.state.toString().matches('APPROVED') ); 
    }
    
    boolean isEditAllowed() {
        if ( !super.isEditAllowed() ) return false; 
        return !entity.state.toString().matches('APPROVED'); 
    }
    
    void approve() { 
        if ( MsgBox.confirm('You are about to approve this account. Proceed?')) { 
            getPersistenceService().update([ 
               _schemaname: getSchemaName(), 
               objid : entity.objid, 
               state : 'APPROVED' 
            ]); 
            loadData(); 
        }
    }
    
    void disapprove() {
        if ( MsgBox.confirm('You are about to approve this account. Proceed?')) { 
            getPersistenceService().update([ 
               _schemaname: getSchemaName(), 
               objid : entity.objid, 
               state : 'DRAFT' 
            ]); 
            loadData(); 
        }
    }
    
}