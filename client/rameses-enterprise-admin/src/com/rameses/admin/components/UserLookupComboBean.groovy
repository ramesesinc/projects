package com.rameses.admin.controller;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
        
public class OrgController extends CRUDController {
        
    def node, filetype;

    public String getServiceName() { return "OrgAdminService"; }
    
    public String getEntityName() { return node.orgclass; }

    public String getPrefixId() { return "ORG"; }
    
    public String getTitle() {
        if (filetype?.caption) 
            return filetype.caption;
        else 
            return node.orgclass;
    }

    Map createEntity() {
        def e = [orgclass: node.orgclass];
        return e;
    } 

}