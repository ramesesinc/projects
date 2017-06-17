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

class OboVariableModel extends CrudFormModel {
    
    def datatypes = ['integer','decimal','boolean']
        
    @PropertyChangeListener
    def listener = [
        "entity.section" : { o->
            entity.classfication = null;
            binding.refresh("entity.classification");
        }
    ];
    
    def getLookupClassifcations() {
        if(!entity.section) 
            throw new Exception("Entity section is required");
        def h = { o->
            entity.classification = o;
        }
        return Inv.lookupOpener( "obovariable_classification_" +entity.section+ ":lookup", [onselect: h] );
    }
    
    void beforeSave(def mode) {
        entity.objid = entity.name;
    }
    
    
}