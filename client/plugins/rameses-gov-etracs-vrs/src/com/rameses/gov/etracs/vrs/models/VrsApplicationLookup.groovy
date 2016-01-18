package com.rameses.gov.etracs.vrs.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;

public class VrsApplicationLookup {
    
    @Service("VrsApplicationService")
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
        if(!onselect)throw new Exception('Please include handler in VrsApplication Lookup')
        onselect(selectedItem);
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
}