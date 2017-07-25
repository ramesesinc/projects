package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.rulemgmt.constraint.*;
import com.rameses.rulemgmt.*;
import java.rmi.server.*;
import com.rameses.seti2.models.*;

public class MarketListFilter extends CrudListModel { 
        
    def selectedMarket;
    def locations;

    public String getFilterField() {
        return workunit?.info?.workunit_properties?.filterField;
    }
    
    def getMarketLocations() {
        if(!locations) {
            def m = [_schemaname:'market_property'];
            m.select = 'objid,code,name';
            m._start = 0;
            m._limit = 100;
            locations = queryService.getList( m ); 
        }
        return locations;
    };

    @PropertyChangeListener
    def listener  = [ 
        'selectedMarket' : { o->
            listHandler.reload();
        }
    ];
        
    def getCustomFilter() {
        if( !selectedMarket ) {
            return null;
        }
        else {
            return [ getFilterField() + " = :mid", [mid : selectedMarket.objid] ];
        }
    }
        
}