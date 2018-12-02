package com.rameses.enterprise.financial.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
        
class FundModel extends CrudFormModel {

    boolean depositoryFund;
    
   void afterCreate() {
       entity.groupid = caller.selectedNode.groupid;
       entity.system = 0;
       entity.state = 'DRAFT';
       depositoryFund = true;
    }
    
    void afterOpen() {
       depositoryFund = (entity.objid == entity.depositoryfundid );
    }

    @PropertyChangeListener
    def listener = [
        "entity.depositoryfund" : { o->
            entity.depositoryfundid = o.objid;
        },
        "depositoryFund" : { o->
            entity.depositoryfundid = null; 
            entity.depositoryfund  = null;
        }
    ];
    
    void activate() {
        def m = [_schemaname: "fund"];
        m.findBy = [objid: entity.objid];
        m.state = "ACTIVE";
        persistenceService.update( m );
        entity.state = "ACTIVE";
    }
    
    void deactivate() {
        def m = [_schemaname: "fund"];
        m.findBy = [objid: entity.objid];
        m.state = "INACTIVE";
        persistenceService.update( m );
        entity.state = "INACTIVE";
    }

    def getLookupDepositoryFund() {
       def query = [:];
       query.groupid = entity.groupid;
       return Inv.lookupOpener("fund_depository:lookup", [query: query]);
   }     
    
    
}