package com.rameses.gov.etracs.vehicletype.tricycle;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.vehicle.models.*;

public class TricycleAccountForm extends VehicleAccountForm {
    
    public void beforeSave(def mode ) {
        entity.particulars = 'Plate No:'+entity.plateno;
    }

}