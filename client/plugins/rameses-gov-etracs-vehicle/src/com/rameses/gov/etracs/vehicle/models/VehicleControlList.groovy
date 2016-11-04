package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import com.rameses.gov.etracs.vehicle.models.*;

class VehicleControlList extends CrudListModel {

    @Service("VehicleControlService")
    def ctrlSvc;

    def txntypeid;
    
    public def getCustomFilter() {
        return ["txntypeid=:type", [type: txntypeid]]; 
    }
    
    def create() {
        if(!txntypeid)
            throw new Exception("Please select a txntype");
        def q = MsgBox.prompt( "Please enter qty to issue");
        if(!q) return; 
        query.qty = q;
        query.txntype = txntypeid;
        ctrlSvc.generate(query);
        reload();
    }
    
} 