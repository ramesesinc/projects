package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class WaterworksAccountCapture extends CrudFormModel {

    @Service("DateService")
    def dateSvc;

    void afterCreate() {
        entity.address = [:];
        entity.lastreadingyear = dateSvc.getServerYear();
    }

    @PropertyChangeListener
    def listener = [
        "entity.owner": { o->
            entity.acctname = o.name;
            binding.refresh("entity.*");
        }
    ]

    
}