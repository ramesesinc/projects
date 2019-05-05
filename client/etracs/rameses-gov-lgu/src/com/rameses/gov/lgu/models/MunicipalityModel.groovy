package com.rameses.gov.lgu.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.*;

class MunicipalityModel extends CrudFormModel {

    @Script("User")
    def usr;
    
    public String getRootOrgClass() {
        return usr.env.ORGCLASS;
    }
    
    public String getRootOrgCode() {
        return usr.env.ORGCODE;
    }

    def open() {
        if( rootOrgClass == "MUNICIPALITY" ) {
            entity = [objid: usr.env.ORGID];
        }
        return super.open();
    }
    
    void afterCreate() {
        entity.state = 'DRAFT';
        entity.mayor = [title:'MUNICIPAL MAYOR', office:'OFFICE OF THE MUNICIPAL MAYOR'];
        entity.treasurer = [title:'MUNICIPAL TREASURER', office:'OFFICE OF THE MUNICIPAL TREASURER'];
        entity.assessor = [title:'MUNICIPAL ASSESSOR', office:'OFFICE OF THE MUNICIPAL ASSESSOR'];
        entity.root = 0;
        entity.orgclass = "MUNICIPALITY";
        entity.parent = [ objid: usr.env.ORGID, orgclass: usr.env.ORGCLASS, code: usr.env.ORGCODE  ]; 
    }
    
    void afterOpen() {
        if (!entity.mayor) entity.mayor = [:];
        if (!entity.assessor) entity.assessor = [:];
        if (!entity.treasurer) entity.treasurer = [:]; 
    }
    
    @PropertyChangeListener
    def listener = [
        "entity.indexno" : { o->
            def list = [];
            list << rootOrgCode;
            if( entity.indexno ) {
                list << entity.indexno;
            }
            entity.pin = list.join("-");
            entity.code = entity.pin;
            entity.objid = entity.pin.toString().replaceAll('-', '');
        }
    ];
} 
