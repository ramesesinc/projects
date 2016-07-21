package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;
import com.rameses.seti2.models.*;

class IndividualEntityEditNameModel  {
    
    @Service("PersistenceService")
    def persistenceService;

    @Caller
    def caller;
    
    @Binding
    def binding;

    String title = "Edit Name";
    
    def entity;
    
    void init() {
        def old = entity;
        entity = [_schemaname:'entityindividual'];
        entity.objid = old.objid;
        entity.lastname = old.lastname;
        entity.firstname = old.firstname;
        entity.middlename = old.middlename;
    }

    def oncreate = { o->
        persistenceService.update( entity );
        caller.reload();
        binding.fireNavigation("_close");
    }
        
}
