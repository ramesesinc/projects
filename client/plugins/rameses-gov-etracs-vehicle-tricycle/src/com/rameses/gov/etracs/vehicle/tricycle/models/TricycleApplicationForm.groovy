package com.rameses.gov.etracs.vehicle.tricycle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.vehicle.models.*;

public class TricycleApplicationForm extends VehicleApplicationForm {
    
    public void beforeSave(def mode ) {
        entity.particulars = 'Plate No:'+entity.plateno;
    }

}