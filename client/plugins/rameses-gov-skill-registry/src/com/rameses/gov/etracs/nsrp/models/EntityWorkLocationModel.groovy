package com.rameses.gov.etracs.nsrp.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.camera.*;
import com.rameses.seti2.models.*;

class EntityWorkLocationModel extends CrudFormModel
{

    @Service('JobSearchService')
    def svc;

    @Binding
    def binding;

    def locations;

    @PropertyChangeListener
    def listener = [
        "entity.local": {o ->
            if(o) locations = svc.getLocalAddresses();
            if(!o) locations = svc.getNonLocalAddresses();
            entity.location = null;
            binding.refresh("entity.*");
        }
    ];

    void doOpen(){
        super.open();
        if(entity.local) locations = svc.getLocalAddresses();
        if(!entity.local) locations = svc.getNonLocalAddresses();
    }

    void afterCreate() {
        entity.entityid = caller?.masterEntity?.objid;
        entity.local = true;
        locations = svc.getLocalAddresses();
    }
}