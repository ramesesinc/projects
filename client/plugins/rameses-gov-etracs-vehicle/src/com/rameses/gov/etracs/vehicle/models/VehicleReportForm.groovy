package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*; 

public class VehicleReportForm extends CrudReportModel {
    
    public def getReportData() { 
        return entity;
    }
    
}