package com.rameses.admin.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.constant.*;
import com.rameses.seti2.models.*;


class OrgListModel extends CrudListModel {
    
    def orgclass;
    def parentclass;
    def handler;

    @FormTitle
    String title;
    
    void afterInit() {
        query.orgclass = orgclass;
        query.parentclass = parentclass;
    }
    
    /*
     String entityName;
            String parentclass;
            String handler;
            String serviceName = "OrgService";
            
            @FormTitle
            String title;
            
            @FormId
            String id;
            
            public void init() {
                query.orgclass = entityName;
                id = entityName;
            }
            
            public def open() {
                if(!handler) handler = "org";
                def e = getSelectedEntity();
                def opener = Inv.lookupOpener( handler+":open",  [entity:e,title:title, parentclass:parentclass ] );
                opener.target = 'popup';
                return opener;
            }
            
            public Opener create() {
                if(!handler) handler = "org";
                def opener = Inv.lookupOpener( handler+":create",  [entityName:entityName.toUpperCase(), parentclass:parentclass, title:title] );
                opener.target = 'popup';
                return opener;
            }
    */
}       