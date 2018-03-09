package com.rameses.admin.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.enterprise.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.rmi.server.UID;
         
public class SecurityGroupModel {
    
    @Service('QueryService')
    def querySvc; 
            
    def entity;
    def permissions;
    def objectList;
    def handler;
    def selectedObject;

    public void init() {
        def m = [_schemaname: 'sys_usergroup_permission'];
        m.where = [ "usergroup.objid = :roleid", [roleid: entity.usergroup.objid ] ];
        permissions = querySvc.getList(m);
        
        permissions.each {
            if( entity.exclude ) {
                def str = it.object + "." + it.permission;
                if( str.matches(entity.exclude) ) {
                    it.selected = false;
                }
                else {
                    it.selected = true;
                }
            }
            else {
                it.selected = true;
            }
        }
        objectList = permissions*.object.unique().collect{ [name: it] }.sort{};
        objectList.each { obj->
            obj.permissions = permissions.findAll{ it.object == obj.name }.sort{ it.permission }
        }
    }
    
    def permissionListModel = [
        fetchList: {
            if( !selectedObject ) return [];
            return selectedObject.permissions;
        }
    ] as EditorListModel;
    
    def doOk() {
        if(!handler) throw new Exception("Please pass handler")
        def selectionMap = permissions.findAll{ it.selected == false }.groupBy{ it.object };
        def arr = [];
        selectionMap.each { k,v->
            arr << k + "\\.("  +  v*.permission.join("|")  + ")";
        }
        if(arr) {
            handler( arr.join("|") );
        } 
        else {
            handler( "" );
        }
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
    
}
        