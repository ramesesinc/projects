package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class EntityCTCListModel extends CrudListModel {
    
    def getMasterEntity() {
        return caller?.entity;
    }
    
    def getCustomFilter() {
        return ["entityid = :entityid", [entityid: masterEntity?.objid.toString()]];
    }    
}
