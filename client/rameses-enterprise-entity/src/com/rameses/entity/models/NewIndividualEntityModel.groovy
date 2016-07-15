package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;
import com.rameses.seti2.models.*;

class NewIndividualEntityModel  {
    
    @Service("PersistenceService")
    def persistenceService;
    
    @Binding
    def binding;

    String title = "Add New Individual Entity";
    
    boolean saveAllowed = true;
    def entity;
    
    void create() {
        entity = [:];
        entity.objid = 'IND'+new UID();
        entity.address = [:];
    }

    def oncreate = { o->
        binding.fireNavigation("entry");
    }
    
    def onselect;

    def save() {
        entity._schemaname = 'entityindividual';
        entity = persistenceService.create( entity );
        if( onselect ) {
            onselect(entity);
        }    
        MsgBox.alert("Record successfully saved. Entity No is " + entity.entityno );
        return "_close";
    }

    
}
