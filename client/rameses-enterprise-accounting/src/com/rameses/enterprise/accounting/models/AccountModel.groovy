package com.rameses.enterprise.accounting.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

class AccountModel extends CrudFormModel {

    void afterCreate() {
        entity.maingroup = caller.entity;
        entity.group = caller.selectedNode.item.item;
    }    

    public Map createItem(String name, Map subSchema ) { 
        def item = super.createItem( name, subSchema ); 
        item.code = caller.selectedNode.item.item.code;
        item.maingroup = caller.entity;
        item.acctid = entity.objid;
        item.group = caller.selectedNode.item.item;
        return item; 
    }

}
    