package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
        
class FundModel extends CrudFormModel {

   void afterCreate() {
       entity.groupid = caller.selectedNode.objid;
       entity.system = 0;
       entity.state = 'DRAFT';
    }

    @PropertyChangeListener
    def listener = [
        "entity.depositoryfund" : { o->
            entity.depositoryfundid = o.objid;
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