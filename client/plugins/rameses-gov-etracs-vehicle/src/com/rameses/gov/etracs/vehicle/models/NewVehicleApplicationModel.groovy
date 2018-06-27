package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

public class NewVehicleApplicationModel extends CrudFormModel {
    
    @Service("DateService")
    def dateSvc;

    def vehicletype;

    public void afterCreate() {
        entity.vehicletypeid = vehicletype.objid;
        entity.apptype = "NEW";
        entity.txnmode = "ONLINE";
	entity.appyear = dateSvc.getServerYear();
        entity.appdate = dateSvc.getBasicServerDate();
    }

    @FormTitle
    public String getTitle() {
        return "New Application (" + vehicletype.title + ")"; 
    }

    @FormId
    public String getFormId() {
        return vehicletype.objid + ":new"; 
    }

    void afterSave() {
        def op = Inv.lookupOpener( "vehicle_application:open", [ entity: [objid: entity.objid] ] );
        binding.fireNavigation( op );
    }

    
}