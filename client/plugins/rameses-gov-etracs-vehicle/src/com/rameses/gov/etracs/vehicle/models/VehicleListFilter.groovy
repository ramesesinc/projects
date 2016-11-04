package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;

public class VehicleListFilter extends CrudListModel {
    
    @PropertyChangeListener
    def listener = [
        'query.txntype' : { o->
            reload();
        }
    ];

    public def getCustomFilter() {
        return ["txntypeid=:type", [type: query.txntype?.objid]]; 
    }
    
}