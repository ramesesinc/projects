package com.rameses.gov.treasury.report.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.CrudFormModel;
        
class CashbookRevolvingFundModel extends CrudFormModel {
    
    boolean isAllowMove() {
        if ( mode != 'read') return false; 
        if ( entity.state != 'POSTED' ) return false; 
        return (entity.issueto?.objid == OsirisContext.env.USERID); 
    }
    boolean isAllowRevert() {
        if ( mode != 'read') return false; 
        if ( entity.state != 'CANCELLED' ) return false; 
        return (entity.issueto?.objid == OsirisContext.env.USERID); 
    }
    
    void moveToCancelled() { 
        def m = [label: 'Reason for Cancellation']; 
        m.handler = { msg-> 
            def upd = [_schemaname: getSchemaName(), _action: 'CHANGE_STATE' ]; 
            upd.findBy = [objid: entity.objid]; 
            upd.state = 'CANCELLED'; 
            upd.remarks = msg; 
            persistenceSvc.update( upd ); 
            
            reloadEntity(); 
            
            if ( hasCallerMethod('refresh')) {
                caller.refresh(); 
            }
        }
        Modal.show('remark_message:create', m); 
    }
    
    void revert() {
        def m = [label: 'Revert Reason']; 
        m.handler = { msg-> 
            def upd = [_schemaname: getSchemaName(), _action: 'CHANGE_STATE' ]; 
            upd.findBy = [objid: entity.objid]; 
            upd.state = 'POSTED'; 
            upd.remarks = msg; 
            persistenceSvc.update( upd ); 
            
            reloadEntity(); 
            
            if ( hasCallerMethod('refresh')) {
                caller.refresh(); 
            }
        }
        Modal.show('remark_message:create', m); 
    }
    
    public void afterCreate() { 
        def env = OsirisContext.env; 
        entity.remarks = ""; 
        entity.filedby = [objid: env.USERID, name: env.FULLNAME];
        entity.issueto = [objid: env.USERID, name: env.FULLNAME];
    }
}        