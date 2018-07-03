package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*; 
import com.rameses.treasury.common.models.*; 

public class VehicleReportForm extends BasicBillingReportModel {
    
    public def preview() {
        def entity =  caller.entityContext;
        query.objid = entity.objid;
        query.vehicletype = entity.vehicletype;
        return super.preview();
    }
    
    
    
}