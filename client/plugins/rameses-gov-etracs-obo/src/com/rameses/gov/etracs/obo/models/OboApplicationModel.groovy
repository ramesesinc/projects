package com.rameses.gov.etracs.obo.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.enterprise.models.*;

class OboApplicationModel extends WorkflowTaskModel {
    
    def vehicletype;
    def vehicleTypeHandler;
    def ruleExecutor;
    
    InvokerFilter sectionFilter = { inv->
        return entity.permits.find{ it.permittype == inv.properties.section }!=null;
    } as InvokerFilter;
    
    public def open() {
        def retval = super.open();
        //vehicletype = workunit.info.workunit_properties.vehicletype;
        //vehicleTypeHandler = Inv.lookupOpener("vehicle_type_handler:"+vehicletype, [entity:entity]); 
        //ruleExecutor = new RuleProcessor(  { p-> return assessmentService.assess(p) } );
        return retval;
    }
    
    
    String getFormName() {
        return getSchemaName() + ":form";
    }
    
    String getBarcodeFieldname() {
        return "appno";
    }
    
    public String getTitle() {
        return entity.appno + " - " + task?.title;
    }
    
    public String getWindowTitle() {
        return entity.appno;
    }
    
    public String getFormId() {
        return entity.objid;
    }
    
    public def addAuxiliaryPermit() {
        def h = { o->
            if(!entity.permits) entity.permits = [];
            entity.permits << o;
            binding.refresh();
        }
        return Inv.lookupOpener("obo_auxiliary_permit:create", [handler: h, app:entity] );
    }
    
}