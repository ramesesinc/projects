package com.rameses.enterprise.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class EntityAddressModel extends ComponentBean {

    @Binding
    def binding;
    
    def tag;
    
    def _address;
    
    public def getAddress() {
        if(!_address) {
            if(getValue()==null) setValue( [:] );
            _address = getValue();
        }
        return _address;
    }
    
    def editAddress() {
        def h = { o->
            address.text = o.text;
            binding.refresh();
        }
        return Inv.lookupOpener( "address:editor", [handler:h, entity:address, tag: tag] );
    }
    
}
