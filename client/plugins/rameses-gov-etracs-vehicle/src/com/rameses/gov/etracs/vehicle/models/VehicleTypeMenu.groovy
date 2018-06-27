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

public class VehicleTypeMenu {
    
    @Service("QueryService")
    def queryService;
    
    def vehicleTypes;
    def vehicleType;

    String title = "Vehicle Registration";

    void init() {
        def m = [_schemaname:'vehicletype'];
        m.where = ["1=1" ];
        vehicleTypes = queryService.getList(m);
    }

    def listModel = [
        fetchList: { o->
            return vehicleTypes.collect{ [ name: it.objid, caption: it.title, handler: it.guihandler, 
            icon: 'home/icons/' + it.objid + '.png', object: it ]  }
        },
        onOpenItem : { o ->
            return Inv.lookupOpener( "vehicle_menu_categiory", [vehicletype:o.object] ); 
        }
    ] as TileViewModel;

    
}