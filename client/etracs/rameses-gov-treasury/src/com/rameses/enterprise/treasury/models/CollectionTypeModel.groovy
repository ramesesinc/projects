package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
        
public class CollectionTypeModel extends CrudFormModel {

   
    def formTypes;
    def categoryList;
    def handlers;
    def batchHandlers;
    def selectedAccount;
    def orgListHandler;
    
    void afterCreate() { 
        entity.state = 'ACTIVE';
        entity.sortorder = 0; 
        entity.allowonline = 1;
        entity.allowoffline = 0;
        entity.allowbatch = 0;
    } 

    void afterOpen() {
        if(!entity.allowonline) entity.allowonline = 0;
        if(!entity.allowoffline) entity.allowoffline = 0;
        if(!entity.allowbatch) entity.allowbatch = 0;
    }
    
    void afterInit() {
        def m = [_schemaname:'af']
        m.select = "objid";
        m._limit = 1000;
        formTypes = queryService.getList(m)*.objid;
        handlers = InvokerUtil.lookupOpeners( "collectiontype:handler" )*.properties.name;
        batchHandlers = InvokerUtil.lookupOpeners( "collectiontype:batchhandler" )*.properties.name;
    }
    
    def categoryModel = [
        fetchList: { o->
            return categoryList;
        },
        onselect: { o->
            entity.category = o.category;
        }
    ] as SuggestModel;
    
    void addOrg() {
        def h = { arr-> 
            try { 
                arr.each {o-> 
                    def item = [ _schemaname: "collectiontype_org" ];
                    item.objid = entity.objid + ":" + o.objid;
                    item.collectiontypeid = entity.objid;
                    item.org = o;
                    item.org.type = o.orgclass;
                    persistenceService.save( item );
                }
            } finally {  
                orgListHandler.reload(); 
            }  
        } 
        Modal.show( "org:lookup", [onselect: h, multiSelect: true] );
    }


}