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
    
    def getParent() {
        return caller.getMasterEntity();
    }
    
    def handler;
    int year;
    
    @PropertyChangeListener
    def listener = [
        "entity.(reading|prevreading)" : { o-> 
            def curr = (entity.reading==null? 0 : entity.reading);
            def prev = (entity.prevreading==null? 0 : entity.prevreading);            
            entity.volume = curr - prev; 
        }
    ];

    void afterCreate() {
        entity.year = dateSvc.getServerYear();
        entity.readingmethod = 'CAPTURE';
        entity.acctid = parent.objid;
        year = entity.year;
        entity.each{ k,v-> 
            println '>> '+ k + '='+ v + ', '+ (v ? v.getClass(): null);
        }
    }
    
    void afterOpen() {
        entity.acctid = entity.account?.objid;
        year = entity.billingcycle.year;
    }
    
    def getMonthList() {
        if( !year ) return [];
        def m = [_schemaname:'waterworks_billing_cycle'];
        m.findBy = [sectorid:parent.sector.objid, year:year];
        m.select = schema.links.find{ it.name=='billingcycle' }.includefields;
        return qryService.getList(m);
    }
    
    void computeAmount() {
        if( !entity.billingcycle?.month ) 
            throw new Exception("Period Month is required!");

        def m = [:];
        m.objid = parent.objid;
        m.volume = entity.volume;
        def r = compSvc.compute(m);
        entity.amount = r;
    }
    
    void beforeSave( def mode ) {
        if( entity.prevreading > entity.reading) 
            throw new Exception("Prev reading must be less than current reading");
        if( entity.prevreading <0 || entity.reading < 0 || entity.volume <0) 
            throw new Exception("Reading,prevreading,volume must be greater than 0");
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
    
}