package com.rameses.enterprise.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class ReligionSuggestModel extends ComponentBean {

    @Service("ListService")
    def service;
    
    def religionLookup = [
        fetchList: { o->
            o.name = 'religion';
            return service.getList(o);
        }
    ] as SuggestModel;
    
    public String getReligion() {
        return getValue(); 
    }
    
    public void setReligion(String s) {
        setValue( s ); 
    }
    
}
