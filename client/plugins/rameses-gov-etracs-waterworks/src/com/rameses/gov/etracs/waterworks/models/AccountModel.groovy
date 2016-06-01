package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

public class AccountModel extends CrudFormModel {
    
    @Script("BillingCycle")
    def billCycle;
    
    @PropertyChangeListener 
    def changelistener = [
        'entity.metersize' : { o-> 
            entity.meter = null; 
        }
    ];
    
    void afterInit() { 
        listTypes.setHandlers([
            zone : {
                return [sectorid: entity?.sector?.objid]; 
            }
        ]);    
    }
    
    void afterCreate() {
        entity.address = [:];
    }

    def assignStubout() {
        if ( !entity.sector?.objid ) 
            throw new Exception('Please specify sector'); 
        
        boolean pass = false;
        def stuboutid;
        def h = {o->
            stuboutid = o.objid;
            pass = true;
        }
        Modal.show("waterworks_stubout:lookup", [onselect: h, sector: entity.sector] );
        if( !pass) return;

        pass = false;
        h = { o->
            if( o.account?.objid ) throw new Exception("There is already an account assigned. Choose another");
            entity.stuboutnode = o;
            entity.stubout = o.stubout;
            pass = true;
        }
        Modal.show("waterworks_stubout_node_unassigned_account:lookup", [onselect: h, stuboutid: stuboutid] );
        //binding.refresh();
    }

    void computeBillingCycle() {
        if( entity.stuboutnode?.stubout?.objid == null ) {
            throw new Exception("Stubout id is required");
        }
        def e = billCycle.fetch(entity.stuboutnode.stubout);
        entity.billingcycle = e;
    }
    
    def getLookupMeter() { 
        def params = [metersize: entity.metersize];
        if ( !params.metersize ) params.metersize = [objid:null]; 
            
        params.onselect = { o-> 
            entity.meter = o; 
            binding.refresh('entity.meter.*');
        }
        params.onempty = {
            entity.meter = null; 
            binding.refresh('entity.meter.*');
        }
        return Inv.lookupOpener('waterworks_meter_wo_account:lookup', params);
    }
}