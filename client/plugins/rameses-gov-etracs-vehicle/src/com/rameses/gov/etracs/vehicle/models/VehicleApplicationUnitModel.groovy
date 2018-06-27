package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

public class VehicleApplicationUnitModel  {
    
    @Binding
    def binding;

    def vehicletype;
    def mode; 
    def entity;
    def handler;
    def app;

    public void create() {
        entity = [:];
        entity.app = app;
        entity.activeyear = app.appyear;
        mode = "create";
    }

    public void edit() {
        mode = "edit";
        app = entity.app;
    }

    def getLookupFranchise() {
        def p = [:];
        p.onselect = { o->
            entity.franchise = o;
            binding.refresh("entity.franchise");
        };
        p.query = [ vehicletypeid: vehicletype.objid ];    
        return Inv.lookupOpener("vehicle_franchise:available:lookup", p );
    }
    
    def doOk() {
        handler( entity );
        return "_close";
    }

    def doCancel() {
        return "_close";
    }

}