package com.rameses.gov.etracs.obo.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.enterprise.models.*;
import com.rameses.menu.models.MenuCategoryModel;

class OboMenuCategoryModel  extends MenuCategoryModel {

    @Service("QueryService")
    def querySvc;
    
    void loadDynamicItems( String _id, def subitems, def invokers ) {
        if(_id.matches("pc|aux")) {
            def m = [_schemaname: "obo_section_type" ];
            m.findBy = [type: _id.toUpperCase()];
            m.orderBy = "sortindex";
            def list = querySvc.getList( m )
            list.each {
                def id = _id + "/" + it.objid;
                subitems << [ id: id, caption: it.title, index: it.sortindex ];
                invokers.put( id, Inv.lookupOpener("obo_application_section:list", ['query.type': it.objid, 'query.title': it.title ]));
            }
        }
    }
    
}