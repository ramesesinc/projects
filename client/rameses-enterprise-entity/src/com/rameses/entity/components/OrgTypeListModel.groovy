package com.rameses.entity.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class OrgTypeListModel extends ComponentBean {

    def orgTypes = LOV.ORG_TYPES;
    
    public void setOrgType(def ot) {
        setValue( ot );
    }
    
    public def getOrgType() {
        return getValue();
    }
    
}
