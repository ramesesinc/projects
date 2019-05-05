package com.rameses.gov.lgu.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.*;

class DistrictModel extends CrudFormModel {

    @Script("User")
    def usr;
    
    public String getRootOrgClass() {
        return usr.env.ORGCLASS;
    }
    
    public String getRootOrgCode() {
        return usr.env.ORGCODE;
    }

    void afterCreate() {
        entity.orgclass = 'DISTRICT';
        entity.root = 0;
        entity.parent = [ objid: usr.env.ORGID, orgclass: usr.env.ORGCLASS, code: usr.env.ORGCODE  ];
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
