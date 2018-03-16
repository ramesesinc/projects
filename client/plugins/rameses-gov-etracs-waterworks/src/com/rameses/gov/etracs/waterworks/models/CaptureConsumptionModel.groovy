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
    def scheduleSvc;
    
    @Service("DateService")
    def dateSvc;
    
    def dateFormatter = new java.text.SimpleDateFormat('yyyy-MM-dd'); 
    def parent;
    
    def handler;
    
    def hasErrs;
    
    @PropertyChangeListener
    def listener = [
        "entity.(prevreading|reading)" : { o->
            hasErrs = null;
             
            if( o >= entity.meter.capacity ) {
                hasErrs = "Reading must be less than meter capacity"
                throw new Exception("Reading must be less than meter capacity");
            }
            if( entity.reading < entity.prevreading ) {
                entity.volume = (entity.meter.capacity + entity.reading) - entity.prevreading; 
            }
            else {
                entity.volume = entity.reading - entity.prevreading;
            }
            def m = [:];
            m.objid = entity.acctid; 
            m.volume = entity.volume; 
            def r = compSvc.compute(m); 
            entity.amount = r;
            
            m = [scheduleid: entity.account.stuboutnode.schedule.objid, year: entity.year, month: entity.month ];
            def z = scheduleSvc.getSchedule( m );
            if(z) {
                if(z.duedate) entity.duedate = dateFormatter.format(z.duedate);
                if(z.discdate) entity.discdate = dateFormatter.format(z.discdate);
                if(z.readingdate) entity.readingdate = dateFormatter.format(z.readingdate);
            }
        }
    ];

    void afterOpen() {  
        if ( entity.acctid == null ) 
            entity.acctid = entity.account?.objid; 
        if ( parent == null ) {
            parent = (entity.account ? entity.account : [:]); 
            parent.objid = entity.acctid; 
        } 
    }
    
    void beforeSave( mode ) {
        entity.state = "CAPTURE";
        if(hasErrs) {
            throw new Exception("Cannot save entry. /n" + hasErrs);
        } 
    }
    
    void afterSave() {
        if(handler) 
            handler();
        else 
            caller.reload();
    }
   
    
}