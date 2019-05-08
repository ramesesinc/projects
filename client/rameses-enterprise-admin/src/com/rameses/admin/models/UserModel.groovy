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
import java.rmi.server.*;
         
public class UserModel  {
        
    @Service("UserService")
    def service;

    @Binding
    def binding;

    @FormTitle
    def title;
            
    def entity;
    def initiator = 'edit';
    def domainList;
    def selectedDomain;
    def selectedUsergroup;
    def usergroups;
    
    void open() {
        initiator = 'open';
        def o = service.open([ objid: entity.objid ]); 
        if ( o ) entity.putAll( o ); 

        refresh();
    }

    void edit() {
        sectionHandler.setSelectedIndex( 0 ); 
        initiator = 'edit'; 
    }
    
    void cancelEdit() {
        open(); 
    }
    
    void save() {
        def buff = new StringBuilder(); 
        buff.append( entity.lastname ).append(", ").append( entity.firstname ); 
        if ( entity.middlename ) buff.append(" ").append( entity.middlename ); 
        
        entity.name = buff.toString(); 
        service.update( entity ); 
        open();
    }

    void refresh() {
        title = entity.name;
        init();
    }
        
    void initList() {
        usergroups = service.getUsergroups( [objid: entity.objid] );
        domainList = usergroups*.domain?.unique();
    }
            
    void init() {
        initList();
    }
            
    def addUsergroup() { 
        def params = [
            entity:[objid:"UGM"+ new UID(), user: entity], 
            view: 'user',
            saveHandler:{ o->
                if( !usergroups ) usergroups = [];
                usergroups << o; 
                initList();
                binding.refresh();
                usergroupList.reload(); 
            }
        ]; 
        return InvokerUtil.lookupOpener('usergroup:add', params); 
    } 
            
    def editUsergroup() { 
        if(!selectedUsergroup) return;
        def params = [
            view: 'user',
            entity: selectedUsergroup,
            saveHandler: { o->
                initList();
                binding.refresh();
                usergroupList.reload(); 
            }
        ];
        return InvokerUtil.lookupOpener( 'usergroup:edit', params );
    }
            
    void removeUsergroup() { 
        if(!selectedUsergroup) return;
        if( MsgBox.confirm("You are about to remove this entry. Continue?")) {
            service.removeUsergroup(selectedUsergroup);
            usergroups.remove(selectedUsergroup);
            initList();
            usergroupList.reload(); 
        }
    }

    def usergroupList = [
        fetchList: { 
            if(!selectedDomain ) return [];
            return usergroups.findAll{ it.domain == selectedDomain };
        }
    ] as BasicListModel;
    
 
    def resetPassword() {
        return Inv.lookupOpener('user:resetpassword', [entity: entity]); 
    } 
    
    def _sections; 
    def getSections() {
        if ( _sections == null ) {
            _sections = Inv.lookupOpeners('sys_user:sections', [caller: this, entity: entity]); 
        }
        return _sections;
    }
    
    def sectionHandler = [
        getOpeners: {
            return getSections(); 
        }, 
        beforeSelect: { op,idx-> 
            if ( initiator == 'edit' && (idx == 1 || idx==2)) {
                return false; 
            }
            return true; 
        }
    ] as TabbedPaneModel;
}
        