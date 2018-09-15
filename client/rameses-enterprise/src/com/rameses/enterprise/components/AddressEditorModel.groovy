package com.rameses.enterprise.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AddressEditorModel extends ComponentBean {

    @Binding
    def binding;

    def handler;
    def tag;
    def delimiter;

    def editAddress() {
        def h = { o->
            if (handler) handler(o);
            binding.refresh();
        }
        //check first if there are any fields in any one of the address fields.
        return Inv.lookupOpener( "address:editor", [handler:h, entity:value, tag: tag, delimiter: delimiter ] );
    }
    
    
}
