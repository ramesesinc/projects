package com.rameses.clfc.treasury.amnesty.capture

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.*;

class XAmnestyCaptureOfferController 
{
    @Binding
    def binding;
    
    def entity;
    def handler;
    def allowEdit = true;
    def parentid;
    
    @PropertyChangeListener
    def listener = [
        "entity.isspotcash": { o->
            if (o == 0) {
                entity.date = null;
            } else if (o != 0) {
                entity.days = 0;
                entity.months = 0;
            }
            binding.refresh('entity');
        }
    ]
    
    void init(){
       entity = [
           objid        : "AO" + new UID(),
           parentid     : parentid,
           isspotcash   : 0,
           days         : 0, 
           months       : 0
       ];
    }    

    def doOk(){
        entity._edited = true
        if (handler) handler(entity);
        return '_close';
    }

    def doCancel(){
        entity.offer = [:];
        return '_close';
    }
}

