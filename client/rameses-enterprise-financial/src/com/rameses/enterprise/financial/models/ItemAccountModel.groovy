package com.rameses.enterprise.financial.models; 

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class ItemAccountModel extends CrudFormModel {
    
    def listHandler;
    
    void afterCreate() {
        entity.type = getDefaultType();
        entity.active = 1;
    }
    
    public String getDefaultType() {
        return caller.tag;
    }
    
    void addSubItems() { 
        def h = { arr-> 
            def sname = 'itemaccount'; 
            def m = [ _schemaname: sname ]; 
            m.findBy = [ parentid: entity.objid ];
            def orgs = qryService.getList( m ).collect{ it.org?.objid }.findAll{( it )}.unique();  
        
            try { 
                arr.each {o-> 
                    if ( !o ) return; 
                    if ( orgs.contains( o.objid )) return; 
                    def item = [ _schemaname: sname ];
                    item.objid = entity.objid + ":" + o.objid;
                    item.org = o;
                    item.parentid = entity.objid;
                    item.code = entity.code + "-" + o.code;
                    item.title = entity.title + " " + o.name; 
                    item.fund = entity.fund;
                    item.type = entity.type;
                    item.tags = [];
                    item.state = entity.state;
                    persistenceService.create( item );
                }
            } finally {  
                listHandler.reload(); 
            }  
        } 
        Modal.show( "org:lookup", [onselect: h, multiSelect: true] );
    } 

    void removeSubItems() {
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
    
    boolean selectedall = false;
    void selectAll() {
        listHandler.selectAll();
        selectedall = true;
        binding.refresh();
    } 
    
    void deselectAll() {
        listHandler.deselectAll(); 
        selectedall = false;
        binding.refresh();
    }     
    
    void changeToSubAccount() {
        //check first if there are subitems. do not remove if there are.
        def z = [_schemaname:"itemaccount"];
        z.findBy = [parentid: entity.objid];
        z.select = "cnt:{COUNT(objid)}";
        def mm = queryService.findFirst( z );
        if( mm.cnt > 0 ) {
            throw new Exception("Cannot remove account if there are subitems");
        }
        def s = { o->
            if(o.objid==entity.objid) 
                throw new Exception("You cannot transfer account on itself");
            def m = [_schemaname:"itemaccount"];
            m.findBy = [objid: entity.objid];
            m.parentid = o.objid;
            persistenceService.update( m );
            open();
            binding.refresh();
        };
        Modal.show("genericaccount:lookup", [onselect: s], [title:"Transfer to parent account"]);
    }
    
    void changeType() {
        def p = [:];
        p.onselect = { o->
            if(entity.type != o.name) {
                if(!MsgBox.confirm("You are about to change this to another type. This cannot be viewed in this list anymore. Proceed?")) return;
                def m = [_schemaname:"itemaccount"];
                m.findBy = [objid: entity.objid];
                m.type = o.name;
                persistenceService.update( m );
                open();
                binding.refresh();
            }
        };
        p.listHandler = [
            getColumns: {
                return [ [name:'name', caption:'Select an account type'] ];
            },
            fetchList: {
                return [ [name:"REVENUE"],[name:"NONREVENUE"],[name:"PAYABLE"] ];
            }
        ] as BasicListModel;
        Modal.show("simple_list_lookup", p);
    }
    
    void removeParent() {
        //check first if there are subitems. do not remove if there are.
        if(!MsgBox.confirm("You are about to move this out from this parent account. Continjue?")) return ;
        def z = [_schemaname:"itemaccount"];
        z.findBy = [objid: entity.objid];
        z.parentid = "{NULL}";
        persistenceService.update( z );
        open();
        binding.refresh();
    }
    
}