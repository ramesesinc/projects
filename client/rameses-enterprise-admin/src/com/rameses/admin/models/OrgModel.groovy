package com.rameses.admin.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

class OrgModel extends CrudFormModel {

    void afterCreate() {
        entity.orgclass = caller.orgclass.toUpperCase();
        entity.root = 0;
    }
    
    public void beforeSave(o) {
        if(o == 'create') {
            entity.objid = entity.code;
        }
    }

    /*
    def getLookupOrgclass() {
        return Inv.lookupOpener( "org:lookup", [includes:parentclass] );
    }

    Map createEntity() {
         return [orgclass:entityName];
    }
    */
}
