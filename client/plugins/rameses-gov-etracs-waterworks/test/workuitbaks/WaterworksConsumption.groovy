package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class WaterworksConsumption  {
    
    @Service("WaterworksAccountService")
    def acctSvc;
    
    def entity;
    def selectedItem;

    def listModel  = [
        fetchList: { o->
            def list = acctSvc.getConsumption( entity );
        }
    ] as BasicListModel;
    
    def addEntry() {
        def e = [prevreading: 0, classificationid: entity.classificationid];
        return Inv.lookupOpener("waterworks_consumption:entry", [entity:e]);
    }
    
}