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
    
    boolean requires_recompute;
    def handler;
    
    @PropertyChangeListener
    def listener = [
        "entity.(reading|prevreading)" : { o-> 
            requires_recompute = true; 
            def curr = (entity.reading==null? 0 : entity.reading);
            def prev = (entity.prevreading==null? 0 : entity.prevreading);            
            entity.volume = curr - prev; 
            entity.amount = 0.0;
        }
    ];

    void afterCreate() {
        entity.year = dateSvc.getServerYear();
        entity.readingmethod = 'CAPTURE';
        entity.acctid = parent.objid;
    }
    
    void afterOpen() {  
        if ( entity.acctid == null ) 
            entity.acctid = entity.account?.objid; 
            
        if ( parent == null ) {
            parent = (entity.account ? entity.account : [:]); 
            parent.objid = entity.acctid; 
        } 
    }
    
    void computeAmount() {
        if( !entity.month ) 
            throw new Exception("Period Month is required!");
        
        def m = [:];
        m.objid = entity.acctid; 
        m.volume = entity.volume; 
        def r = compSvc.compute(m); 
        entity.amount = r; 
        requires_recompute = false; 
    } 
    
    void beforeSave( mode ) {
        entity.state = "CAPTURE";
        if( entity.prevreading > entity.reading) 
            throw new Exception("Prev reading must be less than current reading");
        if( entity.prevreading <0 || entity.reading < 0 || entity.volume <0) 
            throw new Exception("Reading,prevreading,volume must be greater than 0");
        if ( requires_recompute ) {
            throw new Exception('Reading consumption has changed. Please click the Compute Amount button first.');
        }
    }
    
    void afterSave() {
        if(handler) 
            handler();
        else 
            caller.reload();
    }
    
    /*boolean isEditAllowed() {
        if(entity.readingmethod == 'ONLINE') return false;
        return super.isEditAllowed();
    }*/
    
    boolean isDeleteAllowed() {
        if(entity.readingmethod == 'ONLINE') return false;
        return super.isEditAllowed();
    }
    
    void calcDueDate() {
        def m = [scheduleid: entity.account.zone.schedule.objid, year: entity.year, month: entity.month ];
        def z = scheduleSvc.getSchedule( m );
        entity.duedate = z.duedate;
        entity.discdate = z.discduedate;
    }
    
}