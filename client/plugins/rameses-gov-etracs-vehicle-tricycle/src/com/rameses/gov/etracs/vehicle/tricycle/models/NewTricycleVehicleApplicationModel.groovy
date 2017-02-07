package com.rameses.gov.etracs.vehicle.tricycle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;

public class NewTricycleVehicleApplicationModel extends com.rameses.gov.etracs.vehicle.models.NewVehicleApplicationForm { 

    def printApplication() {
        return Inv.lookupOpener('vehicle_application_tricycle:print', [entity: entity]); 
    }
    
    def printAssessment() {
        return Inv.lookupOpener('vehicle_assessment_tricycle:print', [entity: entity]); 
    }
}