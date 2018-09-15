package com.rameses.enterprise.accounting.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.* 

class AccountMappingModel { 

    @Service("QueryService")
    def querySvc;
    
    @Service("PersistenceService")
    def persistenceSvc;

    @Caller
    def caller;
    
    def entity;
    
    def selectedItem;
    
    void init() {
        entity = caller.entity;
    }
    
    def mappedItemsHandler;
    
    def unmappedItemsHandler = [
        isMultiSelect: {
            return true;
        },
        fetchList : {
            def m = [_schemaname:"itemaccount"];
            m._limit = 1000;
            m.where = [' objid NOT IN (${subquery}) '];
            m.vars = [subquery: "SELECT itemid FROM account_item_mapping WHERE maingroupid = '" + entity.objid + "'"];
            return querySvc.getList(m); 
        }
        
    ] as BasicListModel;
    
    void mapItem() {
        if(!unmappedItemsHandler.selectedValue) 
            throw new Exception("Please select ant least one item to map");
        def h = { o->
            def list = unmappedItemsHandler.selectedValue;
            list.each { sitem ->
                def m = [_schemaname:'account_item_mapping'];
                m.maingroupid = entity.objid;
                m.item = sitem;
                m.account = o;
                persistenceSvc.create(m);
            }
            unmappedItemsHandler.reload();
            mappedItemsHandler.reload();
        }
        Modal.show( "account:lookup", ['query.maingroupid': entity.objid, onselect: h]);
    }

} 

