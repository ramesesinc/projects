package com.rameses.gov.etracs.vehicletype.boat;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.vehicle.models.*;

public class FishboatForm extends VehicleForm  {
    
    /*
    def create() {
        def e = super.create();
        e.engines = [];
    }
    */
    
    def engineListModel = [
        fetchList: { o->
            if(entity.engines==null) entity.engines = [];
            return entity.engines;
        },
        addItem: { o->
            o.parentid = entity.objid;
            entity.engines << o;
        },
        onRemoveItem: { o->
            entity.engines.remove(o);
        }
    ] as EditorListModel; 
    
}