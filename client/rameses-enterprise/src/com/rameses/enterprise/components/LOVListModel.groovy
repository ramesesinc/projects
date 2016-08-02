package com.rameses.enterprise.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class LOVListModel extends ComponentBean {

    @Service("LOVService")
    def lovService;
    
    String listName;
    
    def lovList;
    
    void init() {
        if(!listName ) throw new Exception("Please specify an listName in LOVListModel");
        lovList = lovService.getKeyValues( listName );
        lovList.each {
            println it;
        }
    }
    
    public String getLov() {
        return getValue(); 
    }
    
    public void setLov(String s) {
        setValue( s ); 
    }
    
}
