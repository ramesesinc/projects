package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;

class VehicleUnitListModel extends CrudListModel {

    def open() {
        def op = Inv.lookupOpener("vehicle_franchise:open", [ entity: selectedItem ] );
        op.target = "popup";
        return op;
    }
    
} 