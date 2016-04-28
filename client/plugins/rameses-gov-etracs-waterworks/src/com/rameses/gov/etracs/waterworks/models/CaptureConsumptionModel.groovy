package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class CaptureConsumptionModel  {
    
    @Script("ListTypes")
    def listTypes;
    
    @Service("WaterworksComputationService")
    def compSvc;
    
    @Service("WaterworksAccountService")
    def acctSvc;

    def entity;
    def handler;
    def info = [prevreading:0, reading:0, volume: 0];
    
    @PropertyChangeListener
    def listener = [
        "info.(reading|prevreading)" : { o->
            info.volume = info.reading - info.prevreading;
        },
        "info.(volume|reading)" : { o->
            info.prevreading = info.reading - info.volume;
            if(info.prevreading<0) info.prevreading = 0;
        }
    ];

    void computeAmount() {
        if( !info.month ) 
            throw new Exception("Month is required!");
        def m = [:];
        m.objid = entity.objid;
        m.volume = info.volume;
        def r = compSvc.compute(m);
        info.item = r.item;
        info.amount = r.amount;
        info.remarks = "for month of " + listTypes.months[info.month-1].name + " " + info.year;
    }
    
    def doOk() {
        if( info.prevreading > info.reading) 
            throw new Exception("Prev reading must be less than current reading");
        if( info.prevreading <0 || info.reading < 0 || info.volume <0) 
            throw new Exception("Reading,prevreading,volume must be greater than 0");
        info.account = [objid:entity.objid];
        acctSvc.postConsumption( info );
        if(handler) handler();
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
    
}