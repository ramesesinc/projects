package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;
import java.text.*;
import com.rameses.functions.*;
import com.rameses.seti2.models.*;

public class CaptureConsumptionModel extends CrudFormModel {
    
    @Service("WaterworksComputationService")
    def compSvc;
    
    @Service("DateService")
    def dateSvc;
    
    def dateFormatter = new java.text.SimpleDateFormat('yyyy-MM-dd'); 
    def parent;
    
    def handler;
    def hasErrs;
    
    def txnModes = ["BEGIN_BALANCE", "CAPTURE" ];
    def consumptionUtil;
    
    @FormTitle
    String title = "Monthly Consumption";
    
    void afterInit() {
        consumptionUtil = ManagedObjects.instance.create(ConsumptionUtil.class);
    }
    
    void afterCreate() {
        entity.txnmode = "CAPTURE";
        entity.acctid = masterEntity.objid;
        entity.account = masterEntity;
        entity.meterid = masterEntity.meter?.objid;
        entity.meter = masterEntity.meter;
        entity.prevreading = masterEntity.meter?.lastreading;
        entity.volume = 0;
    }
    
    public def getMasterEntity() {
        return caller.masterEntity;
    }
    
    void afterSave() {
        if(handler) 
            handler();
        else 
            caller.reload();
    }
    
    void calculate() {
        def h = { res->
            entity.putAll(res);
            binding.refresh();
        }
        def p = [ acctid: masterEntity.objid, 
            prevreading: entity.prevreading, 
            reading: entity.reading, 
            volume:entity.volume,
            meterid: masterEntity.meter?.objid,
            meterstate: (masterEntity.meter?.objid==null)? "UNMETERED" : masterEntity.meter?.state 
        ];
        consumptionUtil.compute( p, h);
    }
    
}