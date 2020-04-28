package com.rameses.gov.etracs.rptis.province.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class EntityMappingSectionModel extends CrudListModel {
    @Service('ProvinceEntityMappingService')
    def svc;

    def getMasterEntity() {
        return caller?.entity;
    }
    
    def getCustomFilter() {
        return ["parent_objid = :entityid", [entityid: masterEntity?.objid.toString()]];
    }    

    void removeEntity() {
    	if( !MsgBox.confirm('You are about to delete this record. Proceed?')) {
            return;
        }

        svc.removeEntity(selectedItem)
        reload();
    }
}