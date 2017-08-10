package com.rameses.enterprise.accounting.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.* 

class AccountMappingModel { 

    @Service("AccountMappingService")
    def acctMapSvc;
    
    def searchText;
    def selectedItem;
    
    @Caller
    def caller;
    
    @FormTitle
    public String getTitle() {
        return maingroup.name;
    }
    
    void search() {
        reload();
    }
    
    def getMaingroup() {
        return caller.entity;
    }
    
    def mapModel = [
        fetchList: { o->
            def m = [:];
            m.putAll(o);
            m.maingroupid = maingroup.objid;
            if(searchText) m.searchText = searchText;
            return acctMapSvc.getList(m);
        },
        onColumnUpdate: { o, name ->
            def m =[:]
            m.item = selectedItem?.item;
            m.account = o;
            m.maingroup = maingroup;
            acctMapSvc.mapAccount( m );
        }
        
    ] as EditorListModel;
    
    public void reload() {
        mapModel.reload();
    }
    
    def getAccountLookup() {
        return Inv.lookupOpener( "account:lookup", [maingroupid: maingroup.objid ] );
    }
    

} 

