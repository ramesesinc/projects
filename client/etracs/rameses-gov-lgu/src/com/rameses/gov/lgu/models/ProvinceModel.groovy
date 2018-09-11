package com.rameses.gov.lgu.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.*;

class ProvinceModel extends CrudFormModel {

    @Service("OrgService")
    def orgSvc;
    
    def open() {
        def root = orgSvc.getRoot();
        if( root.orgclass == "PROVINCE") {
            entity = [objid: root.objid ];
        }
        else if( root.orgclass == "MUNICIPALITY" ) {
            entity = [objid: root.parent.objid ];
        }
        else {
            throw new Exception("Province not applicable if root orgclass is not PROVINCE or MUNICIPALITY");
        }
        return super.open();
    }
    
    void afterOpen() {
        if (!entity.governor) entity.governor = [:];
        if (!entity.assessor) entity.assessor = [:];
        if (!entity.treasurer) entity.treasurer = [:]; 
    }
} 
