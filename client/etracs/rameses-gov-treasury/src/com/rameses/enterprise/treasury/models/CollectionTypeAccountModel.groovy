package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
        
public class CollectionTypeAccountModel extends CrudFormModel {

    def collectiontypeid;
    def valueTypes = [ "ANY", "FIXED", "FIXEDUNIT" ];
    def fund;

    void afterCreate() {
        entity.collectiontypeid = collectiontypeid;
    }

    def getLookupAccount() { 
        def params = ['query.fund' : fund ];  
        params.onselect = { o->
            entity.account = o;
        }
        return Inv.lookupOpener( "revenueitem:lookup", params );
    }
    
}