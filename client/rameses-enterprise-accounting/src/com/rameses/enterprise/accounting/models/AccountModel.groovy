package com.rameses.enterprise.accounting.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

class AccountModel extends CrudFormModel {

    def maingroup;
    def parent;
    def type;
    
    void afterCreate() {
        entity.maingroup = maingroup;
        entity.group = parent;
        entity.type = type;
        MsgBox.alert( "main group " + entity.group );
    }    

    void beforeSave( def mode ) {
        if( mode == "create") {
            entity.objid = entity.maingroup.objid + "-" + entity.code;
        }
    }
    
}
    