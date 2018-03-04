package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
        
public class CollectionTypeModel extends CrudFormModel {

    @Service("CollectionTypeService")
    def service;
    
    def formTypes;
    def categoryList;
    def handlers;
    def batchHandlers;
    
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
        formTypes = service.getFormTypes()*.objid;
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
        def params = [ fund: entity.fund ]; 
        params.handler = { o-> 
            o.collectiontypeid = entity.objid; 
            entity.accounts << o; 
            accountModel.reload();
        }
        return Inv.lookupOpener( "collectiontypeaccount:create", params );
    }
    def editAccount() {
        if(!selectedAccount) throw new Exception("Please select an account"); 

        def params = [ fund: entity.fund, entity: selectedAccount ]; 
        params.handler = { o-> 
            accountModel.reload(); 
        } 
        return Inv.lookupOpener( "collectiontypeaccount:edit", params ); 
    }
    def removeAccount() {
        if(!selectedAccount) throw new Exception("Please select an account");
        entity.accounts.remove(selectedAccount); 
        accountModel.reload();
    }


    def accountModel = [
        fetchList: { o-> 
            return entity.accounts; 
        },
        sync: {
            entity.accounts = service.getAccounts( [objid: entity.objid] ); 
            accountModel.reload(); 
        } 
    ] as BasicListModel;


    void afterCreate( o ) {
        accountModel.reload();
    } 
    
    void afterOpen( o ) {
        accountModel.reload();
    } 
    
    void afterSave( o ) {
        accountModel.sync();
    } 


    
}