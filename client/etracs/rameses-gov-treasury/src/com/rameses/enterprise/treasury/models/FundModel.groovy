package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
        
class FundModel extends CrudFormModel {

   void afterCreate() {
        entity.parentid = caller.selectedNode.objid;
        entity.system = 0;
        entity.state = 'DRAFT';
    }
    
    
    
}