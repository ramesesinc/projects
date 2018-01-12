package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*; 

public class VehicleReportForm extends FormReportModel {
    
    public def preview() {
        def entity =  caller.entityContext;
        query.objid = entity.objid;
        query.vehicletype = entity.vehicletype;
        return super.preview();
    }
    
    public def previewByFranchiseId() {
        def entity =  caller.entityContext;
        query.objid = entity.franchise.objid;
        query.vehicletype = entity.vehicletype;
        return super.preview();
    }
    
    
}