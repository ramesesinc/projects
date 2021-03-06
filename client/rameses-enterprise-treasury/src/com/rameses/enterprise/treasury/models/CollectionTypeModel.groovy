package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
        
public class CollectionTypeModel extends CRUDController {

    @Service("CollectionTypeService")
    def service;

    String entityName = "collectiontype";
    String prefixId = "COLLTYPE";
    boolean showConfirmOnSave = false;

    def selectedAccount;
    def selectedFund;

    Map createEntity() { 
        def m = super.createEntity(); 
        m.sortorder = 0; 
        m.allowonline = true;
        m.defaultvalue = 0;
        m.valuetype = 'ANY';
        m.accounts = []; 
        m.funds = []; 
        return m; 
    } 

    def getFormTypes() {
        return service.getFormTypes()*.objid;
    }

    def getHandlers() {
        return InvokerUtil.lookupOpeners( "collectiontype:handler" )*.properties.name;
    }

    def getBatchHandlers() {
        return InvokerUtil.lookupOpeners( "collectiontype:batchhandler" )*.properties.name;
    }

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

    def categoryModel = [
        fetchList: { o->
            return service.getCategories();
        },
        onselect: { o->
            entity.category = o.category;
        }
    ] as SuggestModel;
}