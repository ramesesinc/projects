package com.rameses.gov.lgu.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.*;

class BarangayModel extends CrudFormModel {

    @Script("User")
    def usr;
    
    public String getRootOrgClass() {
        return usr.env.ORGCLASS;
    }
    
    public String getRootOrgCode() {
        return usr.env.ORGCODE;
    }

    void afterCreate() {
        entity.captain = [title:'BARANGAY CAPTAIN', office:'OFFICE OF THE BARANGAY CAPTAIN'];
        entity.treasurer = [title:'BARANGAY TREASURER', office:'OFFICE OF THE BARANGAY TREASURER'];
        entity.parent = [:];
        entity.orgclass = 'BARANGAY';
        entity.state = 'DRAFT';
        entity.root = 0;
        if( rootOrgClass == 'MUNICIPALITY' ) {
            entity.parent = [ objid: usr.env.ORGID, orgclass: usr.env.ORGCLASS, code: usr.env.ORGCODE  ];
        }
    }
    
    @PropertyChangeListener
    def listener = [
        "entity.indexno|entity.parent" : { o->
            def list = [];
            /*
            if( rootOrgClass == 'CITY' ) {
                list << rootOrgCode;
            }
            */
            if( entity.parent?.code ) {
                list << entity.parent.code;
            }
            if( entity.indexno ) {
                list << entity.indexno;
            }
            entity.pin = list.join("-");
            entity.code = entity.pin;
            entity.objid = entity.pin;
        }
    ];
    
} 
