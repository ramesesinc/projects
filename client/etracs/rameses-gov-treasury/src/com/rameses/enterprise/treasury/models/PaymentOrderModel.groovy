package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.* 
import com.rameses.util.BreakException

class PaymentOrderModel extends CrudFormModel {
    
    void afterCreate() {
        entity.items = [];
        boolean pass = false;
        def h = [:];
        h.onselect = { o->
            entity.collectiontype = o;
            pass = true;
        };
        Modal.show( "collectiontype:paymentorder:lookup", h )
        if(!pass) throw new BreakException();
    }
    
    @PropertyChangeListener
    def listener = [
        "entity.payer": { o->
            entity.paidby = o.name;
            entity.paidbyaddress = o.address.text;
            binding.refresh();
        }
    ];
    
    void afterSave() {
        MsgBox.alert( "Order of payment number " + entity.objid );
    }
    
} 