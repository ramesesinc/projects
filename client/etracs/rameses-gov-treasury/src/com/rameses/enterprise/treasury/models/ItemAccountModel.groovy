package com.rameses.enterprise.treasury.models; 

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class ItemAccountModel extends CrudFormModel {
    
    void afterCreate() {
        entity.type = getDefaultType();
        entity.active = 1;
    }
    
    public String getDefaultType() {
        return caller.tag;
    }
    
    /*
    boolean isAllowApprove() {
         return ( mode=='read' && !entity.state.toString().matches('APPROVED') ); 
    }
    boolean isAllowDisapprove() {
         return ( mode=='read' && entity.state.toString().matches('APPROVED') ); 
    }
    
    boolean isEditAllowed() {
        if ( !super.isEditAllowed() ) return false; 
        return !entity.state.toString().matches('APPROVED'); 
    }
    */
    
    void activate() { 
        if ( MsgBox.confirm('You are about to approve this account. Proceed?')) { 
            getPersistenceService().update([ 
               _schemaname: getSchemaName(), 
               objid : entity.objid, 
               state : 'ACTIVE' 
            ]); 
            loadData(); 
        }
    }
    
    void deactivate() {
        if ( MsgBox.confirm('You are about to approve this account. Proceed?')) { 
            getPersistenceService().update([ 
               _schemaname: getSchemaName(), 
               objid : entity.objid, 
               state : 'INACTIVE' 
            ]); 
            loadData(); 
        }
    }
    
    
}