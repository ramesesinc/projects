package com.rameses.enterprise.accounting.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

class AccountModel extends CrudFormModel {

    void afterCreate() {
        entity.maingroup = caller.masterEntity;
    }    

    def getAccountGroupLookup() {
        return Inv.lookupOpener( "account_group:lookup", [maingroupid: entity.maingroup.objid] );
    }        

    public Map createItem(String name, Map subSchema ) { 
        def item = super.createItem( name, subSchema ); 
        item.code = caller.masterEntity.code;
        item.maingroup = caller.masterEntity;
        item.acctid = entity.objid;
        item.group = entity.group;
        return item; 
    }
}
    