package com.rameses.gov.lgu.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.*;

class CityModel extends CrudFormModel {

    @Script("User")
    def usr;
    
    public String getRootOrgClass() {
        return usr.env.ORGCLASS;
    }
    
    public String getRootOrgCode() {
        return usr.env.ORGCODE;
    }

    def open() {
        entity = [objid: usr.env.ORGID];
        return super.open();
    }
    
    void afterOpen() {
        if (!entity.mayor) entity.mayor = [:];
        if (!entity.assessor) entity.assessor = [:];
        if (!entity.treasurer) entity.treasurer = [:]; 
    }
    
} 
