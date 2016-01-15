package com.rameses.clfc.admin.roletemplate

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class RoleTemplateUserGroupController
{
    @Service("RoleTemplateUserGroupService")
    def service;
    
    @Service('SecuritygroupService')
    def secSvc;
    
    def mode = 'create';
    def entity, templateid, saveHandler;
    
    void open() {
        entity = service.open(entity);
    }
    
    void create() {
        entity = [objid: 'RTUG' + new UID()];
    }
    
    def getLookupUsergroup() {
        return InvokerUtil.lookupOpener( "usergroup:lookup", [
            onselect: { o->
                entity.usergroupid = o.objid;
                entity.usergroup = o;
                entity.domain = o.domain;
                entity.role = o.role;
            }
        ]);
    }

    def getLookupOrg() {
        return InvokerUtil.lookupOpener( "org:lookup", [
            onselect:{o->
                entity.org = o;
            }
        ] );
    }
            
    def getSecuritygroups() {
        def usergroupid = entity.usergroup?.objid;
        if (!usergroupid) return null;
        return secSvc.getList([usergroupid: usergroupid]);             
    }

    def showPermissions() {
        def h = { o->
            entity.exclude = o;
        }
        return InvokerUtil.lookupOpener( "securitygroup:custom", 
            [usergroup:entity.usergroup, securitygroup: entity.securitygroup, 
            handler:h, customExclude: entity.exclude, forceExclude: entity.securitygroup?.exclude ] 
        );
    }
    
    def doOk() {
        //println 'templateid ' + templateid;
        //if (templateid) entity.templateid;
        //service.save( entity );
        if(saveHandler) saveHandler(entity);
        return "_close";
    }

    def doCancel() {
        return "_close";
    }
}

