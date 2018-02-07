package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.* 

class PaymentOrderModel extends CrudFormModel {
    
    void afterCreate() {
        //GENERAL COLLECTION must be system based
        entity.collectiontype = [objid: 'GENERAL_COLLECTION'];
        entity.items = [];
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