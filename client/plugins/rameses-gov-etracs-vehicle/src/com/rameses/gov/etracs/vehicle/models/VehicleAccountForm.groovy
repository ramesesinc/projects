package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;

public class VehicleAccountForm extends CrudFormModel {
    
    String title = "Vehicle Account";
    def selectedItem;
    def txntype;
    
    public String getSchemaName() {
        return "vehicle_account";
    }
    
    def create() {
        def z = super.create();
        entity.apptype = 'NEW';
        entity.txntype = txntype;
        entity.operator = [:];
        entity.info = [:];
        title += " " + txntype.objid;
        return z;
    }
    
}