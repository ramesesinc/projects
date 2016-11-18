package com.rameses.gov.etracs.vehicle.tricycle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import com.rameses.gov.etracs.vehicle.models.*;

public class TricycleApplicationForm extends VehicleApplicationForm {
    
    public String getVehicletype() {
        return "tricycle";
    }
    
    def addVehicle() {
        def h = { o->
            o.each { k,v->
                if(k.matches("plateno|engineno|bodyno|sidecarno|make|model|color")) {
                    entity.put(k,v);
                }
            }
            binding.refresh();
        }
        return Inv.lookupOpener("vehicle_tricycle:lookupnew", [handler: h ] )
    }
    
}