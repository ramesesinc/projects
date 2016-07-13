package com.rameses.entity.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.*;

class EntityLookupModel extends ComponentBean {

    @Binding
    def binding;
    
    boolean allowOpen = true;
    boolean allowCreate = true;
    
    def _entity;

    def getLookupEntity() {
        def hs = { o->
            //entity.putAll(o);
            setValue( o ); 
            binding.refresh();
        }
        def he = {
            setValue( null ); 
            binding.refresh(); 
        }
        return Inv.lookupOpener( "entity:lookup", [onselect: hs, onempty: he] );
    }
    
    public def getEntity() {
        return getValue();
    }
    
    public def viewEntity() {
        if( !entity?.objid ) throw new Exception("Please select an entity");
        String type = entity.type;
        def op = Inv.lookupOpener( "entityindividual:open", [entity: entity] );
        op.target = 'popup';
        return op;
    }
    
    public def addEntity() {
        //check if there is role
        boolean b = ClientContext.currentContext.securityProvider.checkPermission("ENTITY", "MASTER","entityindividual.create");
        if(!b) throw new Exception("No sufficient permission to add type");
        
        def s = { o->
            setValue( o ); 
            binding.refresh(); 
        }
        return Inv.lookupOpener( "entityindividual:create", [onselect: s] );
    }
    
}
