package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.camera.*;
import com.rameses.seti2.models.*;

class IndividualEntityRelationModel extends CrudFormModel  {

    @PropertyChangeListener
    def listener = [
        "entity.relateto" : { o->
            entity.relation = null;
        }
    ];

    def getRelationTypeList() {
        if( entity.relateto?.objid == null ) return [];
        def m = [_schemaname:'entity_relation_type'];
        m.where = [ "(gender IS NULL OR gender =:gender )", [ gender: entity.relateto.gender ] ];
        m.orderBy = "objid";
        return queryService.getList( m );
    }

    void afterCreate() {
        entity.entity = caller.masterEntity;
    }

    def doOk() {
        if( entity.entity.objid == entity.relateto.objid )
            throw new Exception("Please do not relate to the same person. ");
        save();
        caller.reload();
        return "_close";
    }

    def doCancel() {
        return "_close";
    }
}
