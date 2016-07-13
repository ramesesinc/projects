package com.rameses.enterprise.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class CitizenshipSuggestModel1 extends ComponentBean {

    @Service("ListService")
    def service;
    
    def citizenshipLookup = [
        fetchList: { o->
            o.name = 'citizenship';
            return service.getList(o);
        }
    ] as SuggestModel;    
    
    public Object getCitizenship() {
        return getValue(); 
    }
    
    public void setCitizenship(Object s) {
        setValue( s ); 
    }
    
}
