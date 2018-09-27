package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import java.rmi.server.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class JuridicalEntityModel extends CrudFormModel {
    
    public def getOrgType() {
        if( caller?.selectedNode?.name ) {
            if( caller.selectedNode.name == "all") return null;
            return caller.selectedNode.name;
        }
        else {
            return null;
        }
    }
    
    void beforeSave( mode ) { 
        entity.remove("ignore_warning"); 
    } 
    
    void afterCreate() {
        entity.objid = "JUR" + new UID();
        entity.orgtype = getOrgType();
    }
    
    def changeName() {
        return "change-name";
    }
}
