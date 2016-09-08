package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.util.*;
import java.rmi.server.*;

class SubItemAccountModel extends com.rameses.seti2.models.CrudListModel {

    @Caller
    def caller;
        
    def getCustomFilter() {
        return ["parentid=:parentid", [parentid: caller.entity.objid] ];
    }

    void afterInit() { 
        multiSelect = true; 
    } 

    void reload() {
        listHandler.reload();
    }

    void addSub() { 
        if ( caller && !caller.allowApprove ) return; 
        
        def h = { arr-> 
            def sname = 'itemaccount'; 
            def m = [ _schemaname: sname ]; 
            m.findBy = [ parentid: caller.entity.objid ];
            def orgs = qryService.getList( m ).collect{ it.org?.objid }.findAll{( it )}.unique();  
        
            try { 
                arr.each {o-> 
                    if ( orgs.contains( o.objid )) return; 

                    def item = [ _schemaname: sname ];
                    item.org = o;
                    item.parentid = caller.entity.objid;
                    item.code = caller.entity.code + "-" + o.code;
                    item.title = caller.entity.title + " " + o.name; 
                    item.fund = caller.entity.fund;
                    item.type = caller.entity.type;
                    item.tags = [];
                    item.state = caller.entity.state;
                    persistenceService.create( item );
                }
            } finally {  
                listHandler.reload(); 
            }  
        } 
        Modal.show( "org:lookup", [onselect: h, multiSelect: true] );
    } 

    void removeSub() {
        if ( caller && !caller.allowApprove ) return; 
        def values = listHandler.getSelectedValue(); 
        if ( !values ) return; 
        
        if ( MsgBox.confirm('You are about to remove this item(s). Continue?')) {
            def item = [ _schemaname: 'itemaccount', findBy: [:] ];
            
            try { 
                values.each{ o-> 
                    if ( !o ) return; 
                    item.findBy.objid = o.objid; 
                    persistenceService.removeEntity( item ); 
                } 
            } finally { 
                listHandler.reload(); 
            } 
        } 
    }
    
    void doSelectAll() {
        if ( caller && !caller.allowApprove ) return; 
        
        listHandler.selectAll(); 
    } 
    void doDeselectAll() {
        if ( caller && !caller.allowApprove ) return; 
        
        listHandler.deselectAll(); 
    }     
}