package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;
import java.text.*;
import com.rameses.functions.*;

public class CaptureConsumptionModel  {
    
    @Script("ListTypes")
    def listTypes;
    
    @Service("WaterworksComputationService")
    def compSvc;
    
    @Service("WaterworksBillingCycleService")
    def billdateSvc;
    
    @Service("WaterworksAccountService")
    def acctSvc;
    
    @Service("QueryService")
    def queryService;    

    @Caller
    def caller;
    
    @Binding
    def binding;
    
    def dateFormatter = new java.text.SimpleDateFormat('yyyy-MM-dd'); 
    
    def getEntity() {
        caller.getMasterEntity();
    }
    
    def handler;
    def info = [prevreading:0, reading:0, volume: 0, amtpaid: 0, postledger:true];
    def billCycle;
    
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
        if( !info.billingcycle?.month ) 
            throw new Exception("Period Month is required!");
        def m = [:];
        m.objid = entity.objid;
        m.volume = info.volume;
        def r = compSvc.compute(m);
        info.item = r.item;
        info.amount = r.amount;
        info.remarks = "for month of " + listTypes.months[info.billingcycle.month-1].name + " " + info.billingcycle.year;
    }
    
    def getBillingCycles() {
        def m = [_schemaname: 'waterworks_billing_cycle'];
        m.findBy = [sectorid: entity.sector.objid];
        m._start = 0;
        m._limit = 1000;
        return queryService.getList(m);
    }
    
    def doOk() {
        if( info.prevreading > info.reading) 
            throw new Exception("Prev reading must be less than current reading");
        if( info.prevreading <0 || info.reading < 0 || info.volume <0) 
            throw new Exception("Reading,prevreading,volume must be greater than 0");
        info.account = [objid:entity.objid];
        acctSvc.postReading( info );
        if(handler) 
            handler();
        else 
            caller.reload();
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
    
}