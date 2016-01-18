package com.rameses.admin.controller;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
        
public class UserGroupController extends CRUDController {
        
    @Service("SecuritygroupAdminService")
    def sgSvc;

    @SubWindow
    def subwindow;

    //to be injected by the explorer
    def node, filetype;
        
    String serviceName = "UsergroupService"            
            
    @FormTitle
    public String getFormTitle() { 
        return getTitle(); 
    } 

    public String getEntityName() {
        if(node.type) {
            return node.userclass;   
        } else {
            return "usergroup";
        }
    }
    
    public String getTitle() {
        return entity.usergroupid;
    }
    
    Map createEntity() {
        def usergroupid = node.usergroupid;
        if (!usergroupid) usergroupid = entity?.usergroupid;
            
        return [usergroupid: usergroupid ];
    } 

    def getSecurityGroups() {
        return sgSvc.getList([usergroupid:entity.usergroupid]);
    }
    
    def getLookupUser() {
        return InvokerUtil.lookupOpener("user:lookup", [
            onselect: { u->
                entity.user = [objid:u.objid, username:u.username, firstname:u.firstname, lastname:u.lastname];
                entity.jobtitle = u.jobtitle;
            }
        ]);
    }

    public String getPrefixId() {
        return "USRGRP";
    }

    def getLookupOrg() {
        //if( !node.orgclass ) return null;
        return InvokerUtil.lookupOpener( "org:lookup", [
            "query.orgclass":node.orgclass,
            onselect: { o->
                entity.org = [objid:o.objid, orgclass:o.orgclass, name:o.name]
            }
        ]);
    }

    def openSecurityGroup() {
        return InvokerUtil.lookupOpener('user-securitygroup:open', [entity: entity]);
    }
    
    void afterCreate( data ) {
        if (data?.usergroupid && subwindow) {
            subwindow.setTitle( data.usergroupid + ' (New)' ); 
        }
    }
    
    void afterEdit( data ) {
        if (data?.usergroupid && subwindow) {
            subwindow.setTitle( data.usergroupid + ' (Edit)' ); 
        }
    }    
    
    void afterCancel() {
        if (entity?.usergroupid && subwindow) {
            subwindow.setTitle( entity.usergroupid ); 
        }        
    }
    
}