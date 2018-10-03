package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class CollectionGroupAccountModel extends CrudFormModel {

    def valueTypes = [ "ANY", "FIXED", "FIXEDUNIT" ];
    def fund;

    void afterCreate() {
        entity.collectiongroupid = caller.entity.objid;
    }
    
}      
