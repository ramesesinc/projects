package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

public class VehiclePermitReport extends CrudReportModel {

    @Service('VehiclePermitService') 
    def permitSvc; 
    
    def permit;
    def vehicletype;
    
    public String getReportName() {
        return "com/rameses/gov/etracs/vehicle/"+ vehicletype +"/reports/permit.jasper"; 
    }
    
    def openPermit() {
        permit = permitSvc.open([ objid: entity.permit?.objid ]);
        return view(); 
    }     

    public def getReportData() { 
        return permit;
    }
    
}    
