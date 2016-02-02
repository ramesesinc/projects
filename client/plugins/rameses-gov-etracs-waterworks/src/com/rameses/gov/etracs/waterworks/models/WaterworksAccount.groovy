package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;


public class WaterworksAccount extends PageFlowController {
    
    @Service("WaterworksClassificationService")
    def classificationSvc;
    
    String title = "Waterworks Account";
    String schemaName = "waterworks_account";

    boolean forbusiness = false;
    
    def ownerTypes = LOV.ORG_TYPES;
    def classificationTypes;
    def entity;
    
    void init() {
        entity = [:];
        classificationTypes = classificationSvc.getList()*.objid;
    }
    
    def getLookupEntity() {
        def h = { o->
            entity.owner = o;
            binding.refresh("entity.owner.*");
        }    
        String s = (entity.ownertype=='SING'?'individual':'juridical') + "entity:lookup";
        return Inv.lookupOpener( s, [onselect:h] );
    }
    
    def viewOwner() {
        if(!entity.owner?.objid ) throw new Exception("Please select an owner first");
        String s = (entity.ownertype=='SING'?'individual':'juridical') + "entity:open";
        return Inv.lookupOpener( s, [entity: entity.owner] );
    }
    
    def getLookupArea() {
        def h = { o->
            entity.area = o;
            binding.refresh("entity.area.*");
        }    
        return Inv.lookupOpener( "waterworksarea:lookup", [onselect:h] );
    }
   
    
}