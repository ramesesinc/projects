package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class IndividualEntityModel extends CrudFormModel {
    
    def create() {
        mode = "create";
        return Inv.lookupOpener("entityindividual:create");
    }
    
    void changeState(def invoker) {
        String s = invoker.properties.state;
        if(!MsgBox.confirm("Proceed to change state to " + s + "?")) return;
        if( !s) throw new Exception("Please specify state in invoker changeState action");
        def u = [objid: entity.objid, state:s, _schemaname:'entityindividual' ];
        persistenceService.update(u);
        entity.state = s;
        reload();
    }
    
}
