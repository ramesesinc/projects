package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class CaptureAccountModel extends CrudFormModel {

    @Service("WaterworksBillingDateService")
    def billingDateSvc;
    
    void afterCreate() {
        entity.address = [:];
    }

    def assignStubout() {
        def h = {o->
            entity.stubout = o.stubout;
            entity.stuboutindex = o.stuboutindex;
            binding.refresh();
        }
        return Inv.lookupOpener("waterworks_stubout:assign", [handler: h] );
    }
    
    void computeDates() {
        if( !entity.dtstarted ) 
            throw new Exception("Select Last period date first");
        if( !entity.stubout?.objid ) 
            throw new Exception("Please assign a stubout");            
        
        def res = billingDateSvc.getBillingDates( [stubout:entity.stubout, billdate: entity.dtstarted] );
        if(!res)
            throw new Exception("There is no billing date rules fired");
        entity.putAll( res );
    }
    
    
    
    
}