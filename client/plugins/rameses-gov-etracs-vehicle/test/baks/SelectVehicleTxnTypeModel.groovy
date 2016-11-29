package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;

public class SelectVehicleTxnTypeModel  {
    
    @Service("QueryService")
    def qryService;
    
    def homeicon = 'home/icons/folder.png';

    String label = "Select Vehicle";
    
    def listModel = [
        fetchList: { o->
            def list = qryService.getList( [_schemaname:'vehicle_txntype', _limit:100 ]);
            list.each {
                it.caption = it.title;
                if(!it.icon) {
                    it.icon = homeicon;
                }
            }
            return list;
        },
        onOpenItem: { o->
            if(!o.uihandler) 
                throw new Exception("Please specify a uihandler in the vehicle type " + o.objid);
            return Inv.lookupOpener( 'vehicle_transaction:menu', [txntype:o] );
        }
    ] as TileViewModel;
    
    
}