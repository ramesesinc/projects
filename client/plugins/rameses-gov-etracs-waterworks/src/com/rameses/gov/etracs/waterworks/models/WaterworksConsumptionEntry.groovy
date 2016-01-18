package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class WaterworksConsumptionEntry  {
    
    def entity;
    def handler;
    
    @Service("WaterworksComputationService")
    def compSvc;

    @PropertyChangeListener
    def listener = [
        "entity.reading" : { o->
            entity.volume = entity.reading - entity.prevreading;
            def r = compSvc.compute(entity);
            entity.amount = r.amount;
        }
    ];

    def doOk() {
        if(!handler) throw new Exception("Please specify a handler");
        
        handler(entity);
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
    
}