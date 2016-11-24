package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.gov.etracs.vehicle.models.*;
import com.rameses.enterprise.models.*;

public abstract class VehicleAccountForm extends CrudFormModel {
    
    String title = "Vehicle Account";
    def selectedItem;
    
    public abstract String getVehicletype();

    def create() {
        super.create();
        entity.vehicletype = vehicletype;
        entity.owner = [:];
        entity.fees = [];
        return entity;
    }
    
    def feeListModel = [
        fetchList: { o->
            return entity.fees;
        }
    ] as BasicListModel;
    
    def appHistoryModel = [
        fetchList: { o->
            return [];
        }
    ] as BasicListModel;
    
    def violationListModel = [
        fetchList: { o->
            return entity.violations;
        }
    ] as BasicListModel;

    public def assignControl() {
        def s = { o->
            entity.control = o;
            binding.refresh();
        }
        return Inv.lookupOpener( "vehicle_control_" + vehicletype + ":available:lookup", [onselect:s] );
    }
    
}