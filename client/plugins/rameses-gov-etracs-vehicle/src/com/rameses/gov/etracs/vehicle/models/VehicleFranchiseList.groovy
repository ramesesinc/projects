package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import com.rameses.gov.etracs.vehicle.models.*;

class VehicleFranchiseList extends CrudListModel {

    @Service("VehicleFranchiseService")
    def ctrlSvc;

    public String getVehicletype() {
        return workunit.info.workunit_properties.vehicletype;
    }
    
    def create() {
        if(!vehicletype)
            throw new Exception("Please select a txntype");
        def q = MsgBox.prompt( "Please enter qty to issue");
        if(!q) return; 
        query.qty = q;
        query.vehicletype = vehicletype;
        ctrlSvc.generate(query);
        reload();
    }
    
} 