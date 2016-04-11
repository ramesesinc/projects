package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class BillOfMaterialsModel {
    
    @Service("PersistenceService")
    def persistenceSvc;
    
    @Service("QueryService")
    def querySvc;
    
    @Caller
    def caller;
    
    def handler;
    
    def deletedItems = [];
    def bomItems = [];

    def getEntity() {
        return caller.entity;
    }
    
    void init() {
        def m = [_schemaname: 'waterworks_application_bom'];
        m.findBy = [parentid: entity.objid];
        bomItems = querySvc.getList(m);
        
        /*
        def h = { o->
            println "";
        }
        handler = new SubitemHandler(entity, schema, "materials", "code,title", h);
        */
    }
    
    def listHandler = [
        onAddItem: { o->
            bomItems.add( o );
        },
        beforeColumnUpdate: { o, colName, newValue ->
            if(colName == "item") {
                if( bomItems.findAll{it.item.objid == newValue.objid}) {
                    throw new Exception("Item already exists");
                }
            }
            return true;
        },
        onRemoveItem: { o->
           deletedItems.add(o);
           bomItems.remove(o);
        },
        fetchList: { o->
            return bomItems;
        }
    ] as EditorListModel;

    def addQty() {
        init();
        return "add-qty"
    }
    
    def doCancel() {
        return "_close";
    }

    def doOk() {
        def m = [_schemaname: 'waterworks_application', objid: entity.objid];
        m.materials = bomItems;
        m.put("materials::deleted", deletedItems);
        persistenceSvc.update(m);
        return "_close";
    }
    
}