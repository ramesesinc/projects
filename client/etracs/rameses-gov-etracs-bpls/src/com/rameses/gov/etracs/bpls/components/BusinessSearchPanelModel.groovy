package com.rameses.gov.etracs.bpls.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class BusinessSearchPanelModel extends ComponentBean {
        
    @Service("BusinessSearchService")
    def service;
    
    def caller;
    def query = [:];
    boolean searching;
    
    def listModel = [
        getRows : { return 25; },
        fetchList: { o->
            if(!query) return [];
            if(!o) o = [:];
            o.putAll(query);
            return service.getList( o );
        }
    ] as PageListModel;
    
    public def getSelectedItem() {
        return caller.getValue()
    }
    
    public void setSelectedItem(def a) {
        caller.setValue(a);
    }
    
    void search() {
        if( query.bin?.contains(":")) {
            query.bin = query.bin.substring( query.bin.indexOf(":")+1);
            binding.refresh("query.bin");
        }
        searching = true;
        listModel.doSearch();
    }
    
}