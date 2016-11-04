package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;

public class VehicleAccountForm extends CrudFormModel {
    
    String title = "Vehicle Account";
    def selectedItem;
    def txntype;
    
    @Caller
    def caller;
    
    def create() {
        super.create();
        if( txntype==null ) {
            txntype = caller.txntype;
        }
        if(!txntype)
            throw new Exception("txntype is required");
        entity.txntype = txntype.objid;
        entity.owner = [:];
        entity.fees = [];
        return entity;
    }
    
}