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
    
    @Script("ListTypes")
    def listTypes;
    
    @Caller 
    def caller; 
    
    def info = [:];
    def entity;
    def handler;
    
    def txnTypes = ["WFEE", "BOM", "OTHER"];
    
    def doOk() { 
        info.parentid = caller?.getMasterEntity()?.objid;
        info.surcharge = 0.0;
        info.interest = 0.0;
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