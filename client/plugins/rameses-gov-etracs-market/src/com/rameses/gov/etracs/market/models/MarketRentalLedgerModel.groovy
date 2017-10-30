package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
import com.rameses.common.*;
import com.rameses.util.*;

public class MarketRentalLedgerModel  {
    
    @Service("QueryService")
    def querySvc;
    
    @Caller
    def caller;
    
    def parent;
    def selectedRental;
    
    void init() {
        parent = caller.entity;
    }
    
    def rentalListHandler = [
        fetchList : { o->
            def m = [_schemaname:"market_rental_ledger"];
            m.findBy = [acctid: parent.objid ];
            m.orderBy = "year,month";
            return querySvc.getList(m);
        }
        
    ] as BasicListModel;
    
    def paymentListHandler = [
        fetchList: { o->
            if(!selectedRental ) return [];
            def m = [_schemaname:"market_payment_rental"];
            m.findBy = [ledgerid: selectedRental.objid ];
            return querySvc.getList(m);
        }
    ] as BasicListModel;
    
}