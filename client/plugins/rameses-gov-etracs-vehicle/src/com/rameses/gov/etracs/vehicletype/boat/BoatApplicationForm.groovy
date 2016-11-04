package com.rameses.gov.etracs.vehicletype.boat;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.vehicle.models.*;

public class BoatApplicationForm extends VehicleApplicationForm {
    
    def create() {
        def e = super.create();
        e.engines = [];
    }
    
    def engineListModel = [
        fetchList: { o->
            if(entity.engines==null) entity.engines = [];
            return entity.engines;
        },
        addItem: { o->
            o.app = entity;
            entity.engines << o;
        },
        onRemoveItem: { o->
            entity.engines.remove(o);
        }
    ] as EditorListModel; 
    
    public void beforeSave(def mode ) {
        entity.particulars = entity.vesselname;
    }
    
}