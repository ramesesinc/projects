package com.rameses.enterprise.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class ProfessionSuggestModel extends ComponentBean {

    @Service("ListService")
    def service;
    
    def professionLookup = [
        fetchList: { o->
            o.name = 'profession';
            return service.getList(o);
        }
    ] as SuggestModel;
    
    public String getProfession() {
        return getValue(); 
    }
    
    public void setProfession(String s) {
        setValue( s ); 
    }
    
}
