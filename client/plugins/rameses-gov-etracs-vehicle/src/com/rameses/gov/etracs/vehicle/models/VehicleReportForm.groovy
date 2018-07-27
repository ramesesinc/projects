package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*; 
import com.rameses.treasury.common.models.*; 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.ClientContext;


public class VehicleReportForm extends FormReportModel {
    
    String _reportName;
    
    public String getReportName() {
        if(_reportName) return _reportName;
        def entity =  caller.entityContext;
        String s = invoker.properties.reportName;
        String mainPath = "com/rameses/gov/etracs/vehicle/reports";
        _reportName = mainPath + "/" + entity.vehicletype.objid  + "/" + s + ".jasper";
        def u = ClientContext.getCurrentContext().getClassLoader().getResource(_reportName);
        if(!u) {
            _reportName = mainPath + "/" + s + ".jasper";
        }
        return _reportName;
    }
    
    public def preview() {
        def entity =  caller.entityContext;
        query.objid = entity.objid;
        query.vehicletype = entity.vehicletype;
        return super.preview();
    }
    
}