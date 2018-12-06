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
    
    @Service("WaterworksScheduleService")
    def skedSvc;

    @Service("DateService")
    def dateSvc;
    
    def dateFormatter = new java.text.SimpleDateFormat('yyyy-MM-dd'); 
    def parent;
    
    def handler;
    def hasErrs;
    
    final def onlineTxnModes = ["ONLINE"];  
    final def captureTxnModes = ["BEGIN_BALANCE", "CAPTURE" ]; 
    
    def txnModes = onlineTxnModes;
    def consumptionUtil;
    
    @FormTitle
    String title = "Monthly Consumption";
    
    void afterInit() {
        consumptionUtil = ManagedObjects.instance.create(ConsumptionUtil.class);
    }
    
    void afterCreate() {
        txnModes = captureTxnModes;
        
        entity.txnmode = "CAPTURE";
        entity.state = 'POSTED';
        entity.acctid = masterEntity.objid;
        entity.account = masterEntity;
        entity.meterid = masterEntity.meter?.objid;
        entity.meter = masterEntity.meter;
        entity.prevreading = 0;
        entity.reading = 0;
        entity.volume = 0;
        
        if ( masterEntity.meter?.objid ) {
            def p = [_schemaname: 'waterworks_meter'];
            p.findBy = [ objid: masterEntity.meter.objid]; 
            p.select = 'lastreading'; 
            entity.prevreading = queryService.findFirst( p )?.lastreading;  
            if ( !entity.prevreading ) entity.prevreading = 0; 
            entity.reading = entity.prevreading; 
        }
    }

    void afterOpen() {
        txnModes = ( "ONLINE" == entity?.txnmode ? onlineTxnModes : captureTxnModes); 
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
    
    def lookupSchedule() {
        def h = [:];
        h.handler = { o->
            o.scheduleid = masterEntity.stuboutnode.schedule.objid;
            def r = skedSvc.getSchedule( o );
            entity.scheduleid = r.objid;
            entity.schedule = r;
            binding.refresh();
        }
        h.fields = [];
        h.fields << [name: 'year', caption: 'From Year', type:'integer'];
        h.fields << [name: 'month', caption: 'From Month', type:'monthlist', datatype:'monthlist', preferredSize:[100,20]];
        Modal.show("dynamic:form", h, [title:"Specify Schedule"] );
    }
    
}