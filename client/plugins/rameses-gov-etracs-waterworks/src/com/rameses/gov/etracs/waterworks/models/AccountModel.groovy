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
        def h = {o->
            entity.stubout = o;
            pass = true;
        }
        Modal.show("waterworks_stubout:lookup", [onselect: h] );
        if( !pass) return;

        pass = false;
        h = { o->
            if( o.acctid ) throw new Exception("There is already an account assigned. Choose another");
            entity.stuboutnode = o;
            pass = true;
        }
        Modal.show("waterworks_stubout_node_unassigned:lookup", [onselect: h, stuboutid: entity.stubout.objid] );
        if(!pass) {
            entity.stubout = null;
        }
        binding.refresh();
    }

    void computeBillingCycle() {
        def e = billCycle.fetch(entity.stubout);
        entity.billingcycle = e;
    }
    
    
}