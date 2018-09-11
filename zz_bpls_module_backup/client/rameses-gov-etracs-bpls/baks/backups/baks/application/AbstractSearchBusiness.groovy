package com.rameses.gov.etracs.bpls.application;
import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.rulemgmt.constraint.*;
import com.rameses.rulemgmt.*;
import java.rmi.server.*;

public abstract class AbstractSearchBusiness extends PageFlowController {

    @Binding
    def binding;

    @Service("BusinessLookupService")
    def lookupSvc;

    @Service("BusinessApplicationService")
    def appSvc;

    def entity;
    boolean searching;
    def query;
    def selectedApplication;
    boolean OPENED_FROM_APP = false;

    public abstract String getAppType();

    void init() {
        searching = false;
        query = [:];
    }

    def start() {
        return super.start('create');
    }
            
    def openFromBusiness() {
        entity = appSvc.initNew( [businessid:entity.objid, apptype:appType] );
        OPENED_FROM_APP = true;
        return super.start('open');
    }

    //done after selection
    def openApplication() {
        if(!selectedApplication)
            throw new Exception("Please select an application");
        entity = appSvc.initNew( [businessid:selectedApplication.objid, apptype:appType] );
    }
            
    def searchListModel = [
        getRows : { return 25; },
        fetchList: { o->
            if(!query) return [];
            if(!o) o = [:];
            o.putAll(query);
            return lookupSvc.getList( o );
        }
    ] as PageListModel;

    void doSearch() {
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