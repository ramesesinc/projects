package com.rameses.gov.etracs.bpls.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class BusinessOrgTypeListModel extends ComponentBean {
        
    def orgTypes = LOV.BUSINESS_ORG_TYPES;
    
    public def getOrgtype() {
        return getValue();
    }
    
    public void setOrgtype(def a) {
        setValue(a);
    }
    
    
}