package com.rameses.clfc.collection.overage.lookup;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class OverageWithInfoLookupController extends BasicLookupModel 
{
    @Service("LoanCollectionOverageLookupService")
    def service;

    def selectedItem;
    def state;
    def withbalance;
    
    def listHandler = [
        fetchList: { o->
            if (state) o.state = state;
            if (withbalance) o.withbalance = withbalance;
            return service.getList(o);
        }
    ] as PageListModel;
    
    boolean show(String searchtext) { 
        listHandler.searchtext = searchtext; 
        return true; 
    }

    def getValue() { 
        return listHandler.selectedValue; 
    } 

    def doSelect() {
        onselect(selectedItem);
        return super.select();
    }

    def doCancel() {
        return super.cancel();
    }

}