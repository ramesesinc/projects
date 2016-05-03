package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

public class AccountModel extends MdiFormModel {
    
    @Service("WaterworksBillingDateService")
    def billingDateSvc;
    
    void afterCreate() {
        entity.address = [:];
    }

    def assignStubout() {
        def h = {o->
            entity.stubout = o.stubout;
            binding.refresh();
        }
        return Inv.lookupOpener("waterworks_stubout:lookup", [handler: h] );
    }
    
    void computeDates() {
        def h = { o->
            entity.dtstarted = o;
            if( !entity.stubout?.objid ) 
                throw new Exception("Please assign a stubout");            

            def res = billingDateSvc.getBillingDates( [stubout:entity.stubout, billdate: entity.dtstarted] );
            if(!res)
                throw new Exception("There is no billing date rules fired");
            entity.putAll( res );
            entity.fromperiod = entity.dtstarted;
            binding.refresh();
        }
        Modal.show("date:prompt", [handler:h]);
    }
    
    
}