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

public class ChangeApplicationForm extends AbstractVehicleEntryForm {
    
    def franchiseno;
    
    void open() {
        if(!entity) throw new Exception("Call the setUp method in ApplicationForm first. Check start action");
        entity.franchiseno = franchiseno;
        entity = applicationService.init( entity );
        afterLoad();
        editmode = 'read';
    }

    def changeUnit() {
        def h = { o->
            entity.vehicle = o;
            entity.vehicleid = o.objid;
            binding.refresh();
        }
        return Inv.lookupOpener("vehicle_" + vehicletype + ":create", [handler: h ] )
    }
    
    def feeListModel = [
        fetchList: { o->
            return entity.fees;
        }
    ] as BasicListModel;
    
    
    
}