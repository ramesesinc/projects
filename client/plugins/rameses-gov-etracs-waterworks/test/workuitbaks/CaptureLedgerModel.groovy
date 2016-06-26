package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class CaptureLedgerModel  {

    @Service("WaterworksLedgerService")
    def svc;

    @Service("WaterworksBillingCycleService")
    def billCycleSvc;

    @Service("DateService")
    def dateSvc;
     
    @Script("ListTypes")
    def listTypes;
    
    @Caller 
    def caller; 
    
    def info;
    def entity;
    def handler;
    
    def txnTypes = LOV.WATERWORKS_LEDGER_TYPE*.key

    public void init() {
        info =  [:];
        info.year = dateSvc.getServerYear();
    }
    
    def getMonthList() {
        if( !info.year ) return [];
        return billCycleSvc.findByYear( [sectorid:caller?.getMasterEntity().sector.objid, year:info.year]);
    }
    
    def doOk() { 
        info.parentid = caller?.getMasterEntity()?.objid;
        svc.post( info ); 
        if ( handler ) { 
            handler(); 
        }
        return "_close"; 
    }
    
    def doCancel() {
        return "_close";
    }
    
}