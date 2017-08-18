package com.rameses.enterprise.accounting.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
        
class AccountGroupModel extends CrudFormModel {

    void afterCreate() {
        entity.maingroup = caller.masterEntity;
        entity.group = null;
    }    

    def getAccountGroupLookup() {
        return Inv.lookupOpener( "account_group:lookup", [maingroupid: entity.maingroup.objid] );
    } 
}