package com.rameses.admin.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.*;

class OrgDropdownListModel extends ComponentBean {

    @Service("QueryService")
    def qrySvc;
    
    def orgclass;
    
    def _items;
    
    public def getItems() {
        if(!_items) {
            _items = [];
            _items << [objid:null, title:"All"];
            def m = [_schemaname: "sys_org"]
            m.findBy = [ orgclass: orgclass ];
            m.orderBy = "code";
            _items = qrySvc.getList( m );
        }
        return _items;
    }
    
    
} 
