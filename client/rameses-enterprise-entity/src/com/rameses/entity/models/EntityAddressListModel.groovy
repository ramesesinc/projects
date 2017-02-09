package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.camera.*;
import com.rameses.seti2.models.*;

class EntityAddressListModel  {

    @Binding
    def binding;

    @Service("QueryService")
    def qrySvc;
    
    @Service("PersistenceService")
    def persistenceSvc;
    
    def entityid;
    def onselect;
    def onchange;
    def selectedItem;
    
    def add() {
        def h = { o->
            o._schemaname = "entity_address";
            o.parentid = entityid;
            persistenceSvc.create( o );
            listModel.reload();
        }
        return Inv.lookupOpener( "address:editor", [handler:h, entity:[:]] );
    }
    
    def edit() {
        if(!selectedItem) throw new Exception("Please select an item");
        def h = { o->
            o._schemaname = "entity_address";
            persistenceSvc.update( o );
            listModel.reload();
        }
        def m = persistenceSvc.read( [_schemaname:'entity_address', objid:selectedItem.objid] );
        return Inv.lookupOpener( "address:editor", [handler:h, entity:m] );
    }

    def listModel = [
        fetchList: { o->
            def m = [_schemaname: 'entity_address'];
            m.findBy = [ parentid: entityid ];
            m.select = "objid,text,primary";
            return qrySvc.getList(m);
        },
        onColumnUpdate: { o,col->
            if(!selectedItem) throw new Exception("Please select an item");
            def m = [_schemaname: 'entity'];
            m.objid = entityid;
            m.address = [objid: selectedItem.objid, text: selectedItem.text]; 
            persistenceSvc.update(m);
            if(onchange) onchange(selectedItem);
            listModel.reload();
        }
    ] as EditorListModel;
    
    def doOk() {
        if(!selectedItem) throw new Exception("Please select an item");
        if(onselect) onselect( selectedItem );
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
    
}
