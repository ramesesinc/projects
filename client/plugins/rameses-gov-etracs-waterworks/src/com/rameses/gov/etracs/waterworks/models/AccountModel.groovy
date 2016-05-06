package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

public class AccountModel extends MdiFormModel {
    
    @Script("BillingCycle")
    def billCycle;
    
    void afterCreate() {
        entity.address = [:];
    }

    def assignStubout() {
        def h = {o->
            entity.stubout = o;
            binding.refresh();
        }
        Modal.show("waterworks_stubout:lookup", [onselect: h] );
    }

    def assignStuboutNode() {
        if(!entity.stubout?.objid) 
            throw new Exception("Please select a stubout first");
        def h = { o->
            if( o.acctid ) throw new Exception("There is already an account assigned. Choose another");
            entity.stuboutnode = o;
            binding.refresh();
        }
        Modal.show("waterworks_stubout_node_unassigned:lookup", [onselect: h, stuboutid: entity.stubout.objid] );
    }
    
    void computeBillingCycle() {
        def e = billCycle.fetch(entity.stubout);
        entity.billingcycle = e;
    }
    
    
}