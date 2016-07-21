package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;
import com.rameses.seti2.models.*;

class JuridicalEntityEditNameModel  {
    
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
        entity = [_schemaname:'entityjuridical'];
        entity.objid = old.objid;
        entity.name = old.name;
        entity.orgtype = old.orgtype;
    }

    def oncreate = { o->
        persistenceService.update( entity );
        caller.reload();
        binding.fireNavigation("_close");
    }
        
}
