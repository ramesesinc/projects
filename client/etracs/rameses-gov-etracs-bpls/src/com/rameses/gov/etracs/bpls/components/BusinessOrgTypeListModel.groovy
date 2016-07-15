package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class BusinessOrgTypeListModel extends ComponentBean {
        
    @Service("LOVService")
    def lovService;
    
    def orgTypes = LOV.ORG_TYPES;
    
    public String getOrgtype() {
        return getValue();
    }
    
    public void setOrgtype(String a) {
        setValue(a);
    }
    
    
}