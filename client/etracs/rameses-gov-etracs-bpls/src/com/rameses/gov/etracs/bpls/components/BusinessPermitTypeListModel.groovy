package com.rameses.gov.etracs.bpls.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class BusinessPermitTypeListModel extends ComponentBean {
    
    @Service("BusinessPermitTypeService")
    def permitTypeSvc;

    def list;
    
    public def getPermitTypes() {
        if(!list) {
            list = permitTypeSvc.getList();            
        }
        return list;
    }
    
    public def getPermitType() {
        return getValue();
    }
    
    public void setPermitType(def a) {
        setValue(a);
    }
    
    
}