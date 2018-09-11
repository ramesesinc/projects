package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.gov.etracs.bpls.application.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

public class BusinessLessorModel extends CrudFormModel  { 
    
    def _orgTypes;
    public def getOrgTypes() {
        if(!_orgTypes) {
            _orgTypes = [ [key:"INDIVIDUAL", value:"SINGLE PROPRIETORSHIP"] ] + LOV.JURIDICAL_ORG_TYPES + [ [key:"MULTIPLE", value:"MULTIPLE"] ];
            _orgTypes.remove( _orgTypes.find{it.key == "GOV"} );
        }
        return _orgTypes; 
    }
    
    @PropertyChangeListener
    def listener = [
        "entity.orgtype" : { o->
            entity.lessor = [:];
        }
    ]
    
    void afterCreate() {
        entity.lessor = [:];
        if( caller.selectedNode?.name == "gov") {
            entity.government = 1;
            entity.orgtype = "GOV";
        }
        else {
            entity.government = 0;
        }
    }
    
    public String getEntityType() {
        if( entity.orgtype == "INDIVIDUAL" ) {  
            return "individual";
        }   
        else if( entity.orgtype == "MULTIPLE" ) {
            return "multiple";
        }
        else {
            return "juridical:"+entity.orgtype.toLowerCase();
        }
    }
    
    
}