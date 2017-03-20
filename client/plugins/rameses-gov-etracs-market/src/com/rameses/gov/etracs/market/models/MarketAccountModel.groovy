package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.rulemgmt.constraint.*;
import com.rameses.rulemgmt.*;
import java.rmi.server.*;
import com.rameses.seti2.models.*;

public class MarketAccountModel extends CrudFormModel {

    @PropertyChangeListener
    def listener = [
        'entity.unit' : { o->
            entity.rate = o.rate;
            entity.payfrequency = o.payfrequency;
            binding.refresh("entity.(rate|payfrequency)");
        },
        'entity.owner': { o->
            if(entity.acctname==null) {
                entity.acctname = o.name;
                binding.refresh("entity.acctname");
            }
        }
    ];        
    
    void updateLedger() {
        def h = { o->
            def m = [_schemaname: 'market_account'];
            m.objid = entity.objid;
            m.startdate = o.startdate;
            m.partialbalance = o.partialbalance;
            m.partialextbalance = o.partialextbalance;
            getPersistenceService().update(m);
            binding.refresh();
        };
        def e = [:];
        Modal.show( "update_market_ledger", [handler: h, entity: entity] );
    }
    
    
}