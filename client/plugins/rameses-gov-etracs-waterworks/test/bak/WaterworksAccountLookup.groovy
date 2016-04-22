package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;

public class WaterworksAccountLookup {
    
    @Service("WaterworksAccountService")
    def service;
    
    def query = [:];
    def selectedItem;
    def onselect;
    
    def listModel = [
        fetchList: { o->
            o.putAll( query );
            return service.getList(o);
        }
    ] as PageListModel;
    
    void search() {
        listModel.reload();
    }
    
    def doOk() {
        if(!onselect)throw new Exception('Please include onselect in Waterworks Account Lookup')
        onselect(selectedItem);
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
}