package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class EntityCTCModel extends CrudFormModel {
    
    void afterCreate() {
        entity.entityid = caller.masterEntity?.objid;
        entity.barangay = [:];
        entity.lgu = [:];
    }
    
}
