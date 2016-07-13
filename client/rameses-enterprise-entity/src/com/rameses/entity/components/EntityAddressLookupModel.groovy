package com.rameses.entity.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class EntityAddressLookupModel extends ComponentBean implements IAddressLookup {

    @Binding
    def binding;
    
    String entityid;
    
    @Service("PersistenceService")
    def persistenceSvc;
    
    public def getAddress() {
        return getValue();
    }
    
    public void setAddress(def a) {
        setValue(a);
    }
    
    public boolean isShowAddressList() {
        return (entityid!=null);
    }
    
    public def viewAddress() {
        if( !entityid ) throw new Exception("Please select an entity");
        def h = { o->
            address = o;
            binding.refresh();
        };
        def c = { o->
            address = o;
        }
        def op = Inv.lookupOpener( "entity_address:lookup", [entityid: entityid, onselect: h, onchange: c] );
        op.target = 'popup';
        return op;
    }
    
    public def editAddress() {
        if(!address?.objid) {
            def h = { o->
                address = o;
                binding.refresh();
            };
            def m = address;
            if(!m) m = [:];
            return Inv.lookupOpener( "address:editor", [handler:h, entity:m] );
        }
        else {
            def h = { o->
                o._schemaname = "entity_address";
                persistenceSvc.update( o );
                address = o;
                binding.refresh();
            };
            def m = persistenceSvc.read( [_schemaname:'entity_address', objid:address.objid] );
            return Inv.lookupOpener( "address:editor", [handler:h, entity:m] );
        }
        
    }
    
    
}
