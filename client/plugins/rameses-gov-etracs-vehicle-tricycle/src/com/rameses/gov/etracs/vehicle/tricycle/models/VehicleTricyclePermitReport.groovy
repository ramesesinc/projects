package com.rameses.gov.etracs.vehicle.tricycle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

public class VehicleTricyclePermitReport extends CrudReportModel {

    @Service('VehiclePermitService') 
    def permitSvc; 

    def createPermit() {
        entity.permit = permitSvc.create([ appid: entity.objid ]); 
        return view();   
    } 
    
    def openPermit() {
        entity.permit = permitSvc.open([ objid: entity.permit?.objid ]); 
        return view(); 
    }     

    public def getReportData() { 
        return entity;
    }
    
}    
