package com.rameses.clfc.treasury.ledger.amnesty.fix

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class AmnestyFixController 
{
    @Binding
    def binding;
    
    @PropertyChangeListener
    def listener = [
        'type': { o->
            if (xentity) {
                entity.clear();
                entity.putAll(xentity);
            }
            entity.type = o;
        }
    ]
    
    def entity, type, mode = 'read';
    def xentity;
    
    void init() {
        if (entity) {
            xentity = [
                objid       : entity?.objid,
                parentid    : entity?.parentid,
                amnestytype : entity?.amnestytype
            ];
        }
        type = entity?.type;
        binding?.refresh('type');
    }
    
    def getTypeList() {
        def list = [];
        def params = [entity: entity, mode: mode];
        def xlist = Inv.lookupOpeners('ledgeramnesty:fixtype', params);
        def props, item;
        xlist?.each{ o->
            props = o.properties;
            if (props) {
                item = [caption: props.caption, value: props.value, index: props.index];
                list.add(item);
            }
        }
        list?.sort{ it.index }
        return list;
    }
    
    def getOpener() {
        if (!type) return null;
        
        def params = [entity: entity, mode: mode];
        def op = Inv.lookupOpener('fixtype:' + type?.value?.toLowerCase(), params);
        if (!op) return null;
        return op;
    }
}

