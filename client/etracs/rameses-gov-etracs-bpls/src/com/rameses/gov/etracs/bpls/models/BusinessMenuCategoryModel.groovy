package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.gov.etracs.bpls.application.*;
import com.rameses.util.*;
import com.rameses.menu.models.MenuCategoryModel;

public class BusinessMenuCategoryModel extends MenuCategoryModel {

    @Service("QueryService")
    def querySvc;
    
    public boolean isRoot() {
        return (OsirisContext.env.ORGROOT == 1);
    }
    
    public def getOrgId() {
        return OsirisContext.env.ORGID;
    }
    
    void loadDynamicItems( String _id, def subitems, def invokers ) {
        if(_id.matches("support")) {
            def m = [_schemaname: "businessrequirementtype" ];
            m.orderBy = "sortindex";
            m.where = ["org.objid = :orgid", [orgid: orgId]];
            m._start = 0;
            m._limit = 100;
            def list = querySvc.getList( m )
            list.each {
                def id = _id + "/" + it.objid;
                subitems << [ id: id, caption: it.title, index: it.sortindex ];
                def query = [:];
                query.type = it.objid;
                query.title = it.title;
                query.orgid = OsirisContext.env.ORGID;
                invokers.put( id, Inv.lookupOpener("business_application_requirement:list", [query: query]));
            }
        }
    }

}