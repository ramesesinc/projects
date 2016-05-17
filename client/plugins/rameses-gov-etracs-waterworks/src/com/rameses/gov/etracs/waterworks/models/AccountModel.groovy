package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

public class AccountModel extends CrudFormModel {
    
    @Script("BillingCycle")
    def billCycle;
    
    void afterCreate() {
        entity.address = [:];
    }

    def assignStubout() {
        boolean pass = false;
        def stuboutid;
        def h = {o->
            stuboutid = o.objid;
            pass = true;
        }
        Modal.show("waterworks_stubout:lookup", [onselect: h] );
        if( !pass) return;

        pass = false;
        h = { o->
            if( o.account?.objid ) throw new Exception("There is already an account assigned. Choose another");
            entity.stuboutnode = o;
            pass = true;
        }
        Modal.show("waterworks_stubout_node_unassigned:lookup", [onselect: h, stuboutid: stuboutid] );
        //binding.refresh();
    }

    void computeBillingCycle() {
        if( entity.stuboutnode?.stubout?.objid == null ) {
            throw new Exception("Stubout id is required");
        }
        def e = billCycle.fetch(entity.stuboutnode.stubout);
        entity.billingcycle = e;
    }
    
    
}