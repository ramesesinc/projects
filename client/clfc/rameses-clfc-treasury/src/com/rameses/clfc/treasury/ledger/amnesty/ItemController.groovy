package com.rameses.clfc.treasury.ledger.amnesty

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class ItemController 
{
    @Binding
    def binding;
    
    @Service('LedgerAmnestyService')
    def service;
    
    def entity, type, mode = 'read';
    def handler, objid, preventity;
    
    @PropertyChangeListener
    def listener = [
        'type': { o-> 
            entity = [objid: objid];
            entity.amnestytype = o ;
            binding?.refresh('opener');
        }
    ]
    
    void init() {
        objid = 'AI' + new UID();
        entity = [objid: objid];
        mode = 'create';
    }
    
    def copy( src ) {
        def dest = [:];
        dest.putAll(src);
        return dest;
    }
    
    void open() {
        if (entity) {
            preventity = copy(entity);
            //entity = copy(preventity);
            objid = entity?.objid;
        }
        if (entity?.amnestytype) {
            def i = getTypeList().find{ it.value == entity?.amnestytype?.value }
            if (i) {
                type = i;
                binding?.refresh('type');
            }
        }
    }
    
    def getTypeList() {
        def list = [];
        def xlist = Inv.lookupOpeners("ledgeramnesty:type:plugin", [:]);
        def props, item;
        xlist?.each{ o->
            props = o.properties;
            if (props) {
                item = [caption: props.caption, value: props.value];
                list.add(item);
            }
        }
        return list;
    }
    
    def getOpener() {
        if (!type) return null;
        
        def params = [entity: entity, mode: mode];
        def op = Inv.lookupOpener('ledgeramnesty:' + type.value?.toLowerCase(), params);
        if (!op) return null;
        return op;
    }
    
    def cancel() {
        return '_close';
    }
    
    def ok() {
        if (!entity.state) entity.state = 'DRAFT';
        if (handler) handler(entity);
        return '_close';
    }
    
    void approve() {
        if (!MsgBox.confirm('You are about to approve this item. Continue?')) return;
        
        def xentity = [objid: entity?.objid];
        xentity = service.approveItem(xentity);
        if (xentity) {
            entity.putAll(xentity);
        }
    }
    
    void disapprove() {
        if (!MsgBox.confirm('You are about to disapprove this item. Continue?')) return;
        
        def xentity = [objid: entity?.objid];
        xentity = service.disapproveItem(xentity);
        if (xentity) {
            entity.putAll(xentity);
        }
    }
    
    /*
    def doCancel() {
        return '_close';
    }
    */
   
    /*
    def doOk() {
        if (!entity.state) entity.state = 'DRAFT';
        if (handler) handler(entity);
        return '_close';
    }
    */
}

