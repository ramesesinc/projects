package com.rameses.enterprise.financial.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

class AccountModel extends CrudFormModel {

    @Binding 
    def _binding;
    
    def maingroup;
    def group;
    def type;
    
    void afterCreate() {
        entity.maingroup = maingroup;
        entity.group = group;
        entity.type = type;
    }    

    void beforeSave( def mode ) {
        if( mode == "create") {
            entity.objid = entity.maingroup.objid + "-" + entity.code;
        }
    }
    
    void changeParentGroup() {
        def param = [ 'query.maingroupid' : entity.maingroupid ];
        param.onselect = { o-> 
            persistenceSvc.update([
                _schemaname : getSchemaName(), 
                objid       : entity.objid, 
                groupid     : o.objid, 
                group       : [objid: o.objid] 
            ]); 
            entity.group = [
                objid : o.objid, 
                code  : o.code, 
                title : o.title 
            ];
        }
        
        String invtype = "account:lookup-group"; 
        if ( entity.type == 'group' ) {
            invtype = "account:lookup-root-group";
        } else if ( entity.type == 'item' ) {
            invtype = "account:lookup-group";
        } else if ( entity.type == 'detail' ) {
            invtype = "account:lookup-item";
        } 
        
        Modal.show( invtype, param);
        super.reload(); 
    }
    
}
    