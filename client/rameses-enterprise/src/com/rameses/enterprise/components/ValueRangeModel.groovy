package com.rameses.enterprise.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class ValueRangeModel extends ComponentBean {

    def handler;
    def name;
    
    @PropertyChangeListener
    def listener = [
        "val.from": { o->
            if(handler) handler(name+".from", o);
        },
        "val.to": { o->
            if(handler) handler(name+".to", o);
        }
    ];
    
    public Object getVal() {
        Object val = getValue();
        if(val == null ) {
            val = [:];
            setValue(val);
        }
        return val;
    }
}
