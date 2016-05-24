package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.rulemgmt.constraint.*;
import com.rameses.rulemgmt.*;
import java.rmi.server.*;

class MarketBillingAnalyzerModel  {

    @Service("MarketRentalRuleService")
    def rentalSvc;

    @Service("MarketBillRuleService")
    def billSvc;

    @Binding
    def binding;

    def entity = [attributes:[], compromise:[:],totalpaid:0, term:12];
    def unitTypes = LOV.MARKET_UNIT_TYPES*.key;
    def billingTypes = [
        [id:"0", description:"Basic"],
        [id:"1", description:"Compromise"],
    ]

    def selectedAttribute;

    String title = "Market Billing Simulator";
    def items = [];

    def addAttribute()  {
        def onselect = { o->
            if( entity.attributes.contains(o) )
                throw new Exception("Attribute already added");
            entity.attributes << o;
            binding.refresh();
        }
        return Inv.lookupOpener( "marketattribute:lookup", [onselect:onselect] );
    }

    void removeAttribute() {
        if(!selectedAttribute) return;
        entity.attributes.remove(selectedAttribute);
    }

    def listModel = [
        fetchList: { o->
            return items;
        }
    ] as BasicListModel;

    void computeRate() {
        def r = rentalSvc.execute( entity );    
        entity.rate = r.rate;
        entity.paytype = r.paytype;
        entity.total = 0;
        items.clear();
        listModel.reload();
    }

    void runTest() {
        if(!entity.rate)
            throw new Exception("Compute rate first");

        def r = billSvc.execute( entity );
        entity.total = r.total;
        entity.overpayment = r.overpayment;
        items = r.items;
        listModel.reload();
    }
}