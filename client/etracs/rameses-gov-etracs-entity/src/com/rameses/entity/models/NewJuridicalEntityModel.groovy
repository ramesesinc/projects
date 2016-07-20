package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;
import com.rameses.seti2.models.*;

class NewJuridicalEntityModel  {
    
    @Service("PersistenceService")
    def persistenceService;
    
    @Binding
    def binding;

    String title = "Add New Juridical Entity";
    
    boolean saveAllowed = true;
    def entity;
    def onselect;
    def mode = "create";
    
    void create() {
        entity = [:];
        entity.objid = 'JUR'+new UID();
        entity.address = [:];
        entity.administrator = [:];
    }

    def back() {
        return "default";
    }
    
    def oncreate = { o->
        binding.fireNavigation("entry");
    }
    
    def save() {
        entity._schemaname = 'entityjuridical';
        entity = persistenceService.create( entity );
        if( onselect ) {
            onselect(entity);
        }    
        MsgBox.alert("Record successfully saved. Entity No is " + entity.entityno );
        return "_close";
    }
    
}
