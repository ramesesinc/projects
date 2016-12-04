package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;

public class VehicleLookupFilter extends CrudLookupModel {
    
    @PropertyChangeListener
    def listener = [
        'query.vehicletype' : { o->
            reload();
        }
    ];

    public def getCustomFilter() {
        return ["vehicletype=:type", [type: query.vehicletype]]; 
    }
    
}