package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
import com.rameses.util.*;

public class MarketRentalRatesModel  {
    
    @Service("MarketRentalRateService")
    def rateSvc;
    
    @Caller
    def caller;
    
    def rates;
    
    void fetchRates(def m) {
        
        rates = rateSvc.getRates( m );    
    }
    
    void initFromRentalUnit() {
        fetchRates( [unitid: caller.entity.objid] );
    }
    
    void initFromAccount() {
        def m = [unitid: caller.entity.unitid, acctid: caller.entity.objid ];
        if( caller.entity.dtstarted ) {
            def db = new DateBean(caller.entity.dtstarted);
            m.fromyear = db.year; 
        }
        fetchRates( m );
    }

    def listHandler = [
        fetchList: {
            return rates;
        }
    ] as BasicListModel;
    
    def doClose() {
        return  "_close";
    }
    
    
}