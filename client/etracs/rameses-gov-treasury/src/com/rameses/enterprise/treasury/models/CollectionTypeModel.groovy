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
    
    void afterCreate() { 
        entity.state = 'APPROVED';
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
    
    def addAccount() { 
        def params = [ fund: entity.fund, collectiontypeid: entity.objid ]; 
        params.handler = { o-> 
            accountListHandler.reload();
        }
        return Inv.lookupOpener( "collectiontype_account:create", params );
    }
    
    def editAccount() {
        if(!selectedAccount) throw new Exception("Please select an account"); 
        def params = [ fund: entity.fund, entity: selectedAccount ]; 
        params.handler = { o-> 
            accountListHandler.reload(); 
        } 
        return Inv.lookupOpener( "collectiontype_account:edit", params ); 
    }
    
    def removeAccount() {
        if(!selectedAccount) throw new Exception("Please select an account");
        def m = [_schemaname: 'collectiontype_account'];
        m.findBy = [objid: selectedAccount.objid ];
        persistenceService.removeEntity( m );
        accountListHandler.reload();
    }

    void reloadAccount() {
        accountListHandler.reload();
    }
    
    def accountListHandler = [
        fetchList: { o-> 
            def m = [_schemaname: 'collectiontype_account'];
            m.findBy = [collectiontypeid: entity.objid ];
            return queryService.getList( m );
        }
    ] as BasicListModel;

    boolean isEditAllowed() { 
        if ( entity.system == 1 ) return false; 
        return super.isEditAllowed(); 
    }
}