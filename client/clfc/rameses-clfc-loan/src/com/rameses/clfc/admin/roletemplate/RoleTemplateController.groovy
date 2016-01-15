package com.rameses.clfc.admin.roletemplate

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class RoleTemplateController extends CRUDController
{
    String serviceName = "RoleTemplateService";
    String entityName = "roletemplate";
    
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = true;
    
    def prevroles, selectedRole;
    
    Map createEntity() {
        return [
            objid   : 'RT' + new UID(),
            txnstate: 'DRAFT'
        ];
    }
    
    void afterOpen( data ) {
        checkEditable(data);
    }
    
    void checkEditable( data ) {
        if (data.txnstate!='ACTIVE') {
            allowEdit = true;
        } else if (data.txnstate=='ACTIVE') {
            allowEdit = false;
        }
    }
    
    void afterSave( data ) {
        data._addedrole = [];
        data._removedrole = [];
    }
    
    void afterEdit( data ) {
        prevroles = [];
        def item;
        data.roles.each{ o->
            item = [:];
            item.putAll(o);
            prevroles.add(item);
        }
    }
    
    void afterCancel() {
        entity.roles = [];
        entity.roles.addAll(prevroles);
        entity._addedrole = [];
        entity._removedrole = [];
        listHandler?.reload();
    }
    
    def listHandler = [
        fetchList: { o->
            if (!entity.roles) entity.roles = [];
            return entity.roles;
        }
    ] as BasicListModel;
    
    def editRole() {
        if(!selectedRole) return;
        def params = [
            mode        : mode,
            entity      : selectedRole,
            templateid  : entity.objid,
            saveHandler : { o->
                def item = entity.roles.find{ it.usergroupid==o.usergroup?.objid }
                item._edited = true;
                item.
                
                
                selectedRole._edit = true;
                selectedRole.usergroupid = o.usergroup?.objid;
                selectedRole.title = o.usergroup?.title;
                selectedRole.domain = o.domain;
                selectedRole.role = o.role;
                
                listHandler?.reload(); 
            }
        ];
        return InvokerUtil.lookupOpener( 'roletemplateusergroup:edit', params );
    }
    
    /*
    def addRole() {
        def params = [
            templateid  : entity.objid,
            saveHandler : { o->
                if (!o.title) o.title = o.usergroup?.title;
                
                if (!entity.roles) entity.roles = [];
                entity.roles.add(o);
                
                if (!entity._addedrole) entity._addedrole = [];
                entity._addedrole.add(o);
                
                listHandler?.reload();
            }
        ]
        return Inv.lookupOpener('roletemplateusergroup:add', params);
    }
    */
    
    def addRole() {
        def handler = { o->
            println 'role ' + o;
            def item = entity.roles.find{ it.usergroupid==o.usergroup?.objid }
            if (item) throw new Exception("This role has already been selected.");
            
            item = [
                objid       : (o.objid? o.objid : 'RTUG' + new UID()),
                parentid    : entity.objid,
                usergroupid : o.usergroup?.objid,
                org         : o.org,
                secutiygroup: o.securitygroup,
                exclude     : o.exclude,
                title       : o.usergroup?.title,
                domain      : o.domain,
                role        : o.role
            ];
            
            if (!entity._addedrole) entity._addedrole = [];
            entity._addedrole.add(item);
            entity.roles.add(item);
            listHandler?.reload();
        }
        return Inv.lookupOpener('roletemplateusergroup:add', [templateid: entity.objid, saveHandler: handler]);
    }
    
    void removeRole() {
        if (!MsgBox.confirm("You are about to remove this item. Continue?")) return;
        
        if (!entity._removedrole) entity._removedrole = [];
        entity._removedrole.add(selectedRole);
        
        if (entity._addedrole) {
            entity._addedrole.remove(selectedRole);
        }
        entity.roles.remove(selectedRole);
        listHandler?.reload();
    }
    
    void activate() {
        if (!MsgBox.confirm("You are about to activate this document. Continue?")) return;
        
        entity = service.activate(entity);
        checkEditable(entity);
    }
    
    void deactivate() {
        if (!MsgBox.confirm("You are about to deactivate this document. Continue?")) return;
        
        entity = service.deactivate(entity);
        checkEditable(entity);
    }
}

