package com.rameses.gov.etracs.bpls.controller;
import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.rulemgmt.constraint.*;
import com.rameses.rulemgmt.*;
import java.rmi.server.*;

public abstract class AbstractSearchBusinessApplication extends AbstractBusinessApplication {

    @Service("BusinessSearchService")
    def searchSvc;

    @Service("BusinessApplicationService")
    def service;

    boolean searching = false;
    def query = [:];
    def selectedItem;
    
    def searchListModel = [
        getRows : { return 25; },
        fetchList: { o->
            if(!query) return [];
            if(!o) o = [:];
            o.putAll(query);
            return searchSvc.getList( o );
        }
    ] as PageListModel;

    void searchBusiness() {
        if( query.bin?.contains(":")) {
            query.bin = query.bin.substring( query.bin.indexOf(":")+1);
            binding.refresh("query.bin");
        }
        searching = true;
        searchListModel.doSearch();
    }

    void doReset() {
        searching = false;
        query = [:];
        searchListModel.doSearch();
        binding.focus("query.bin");
    }

    
}