package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

public class VehicleApplicationList extends CrudListModel {
    
    String title = "Vehicle Application List";
    
    def txntype;
    
    void init() {
        super.init();
        if(!txntype?.objid) throw new Exception("txntype is required");
        title = txntype.title + " " + title;
    }
    
    public def getCustomFilter() {
        return ["txntypeid = :txntype", [txntype:  txntype.objid ] ];
    }
    
}