package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class WaterworksNewConnection extends PageFlowController {
    
    @Service("WaterworksClassificationService")
    def classSvc;
    
    @FormTitle
    def title = "Waterworks New Connection";

    def classificationList;
    def entity;
    
    def start() {
        def s = super.start();
        entity = [owner:[:]];
        classificationList = classSvc.getList();
        return s;
    }
    
    def viewOwner() {
        if(!entity.owner?.objid)
            throw new Exception("Please select an owner first");
        String t = entity.owner.type.trim().toLowerCase();   
        return Inv.lookupOpener(t+"entity:open", [entity: entity.owner]);
    }
    
}