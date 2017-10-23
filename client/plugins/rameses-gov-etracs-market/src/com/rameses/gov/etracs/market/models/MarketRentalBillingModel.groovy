package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
import com.rameses.common.*;
import com.rameses.util.*;

public class MarketRentalBillingModel extends FormReportModel {
    
    public def preview() {
        def billdate = null;
        Modal.show("date:prompt", [handler: {o->billdate=o} ] );
        query.acctid = caller.entityContext?.objid;
        if(!billdate) {
            throw new BreakException();    
        }
        def s = { o->
            if( o ) {
                 query.filters = o*.objid;
            }
            return null;
        };
        Modal.show( "market_collection_txntype:lookup" , [onselect:s] );
        if(!query.filters) throw new BreakException();
        return super.preview();
    }
    
}