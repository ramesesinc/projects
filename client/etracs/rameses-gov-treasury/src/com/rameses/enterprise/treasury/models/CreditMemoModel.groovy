package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.UID;
import com.rameses.seti2.models.*;
import com.rameses.util.*;


public class CreditMemoModel extends CrudFormModel {

    @Service("CreditMemoService")
    def cmSvc;
    
    def depositoryFundid;
    
    void afterCreate() {
        def collectiontype = null
        def filter = ["allowcreditmemo = 1 AND orgid IS NULL AND state='ACTIVE' "];
        def h = { o->
            collectiontype = o;
            def utils = ManagedObjects.instance.create( CollectionTypeListUtil.class );
            utils.checkHasItems( collectiontype );
        }
        Modal.show("collectiontype:lookup", [customFilter:filter, onselect: h]);
        if(!collectiontype) throw new BreakException();
        entity.type = collectiontype;
        entity.items = [];
        entity.amount = 0;
    }
    
    public def getLookupBankAccount() {
        def p = [:];
        p.fundid = depositoryFundid;
        p.onselect = { o->
            entity.bankaccount = o;
            binding.refresh();
        }
        return Inv.lookupOpener("bankaccount:lookup", p );
    }
    
    def selectedItem;
    
    def getLookupItems() {
        def p = [:];
        p.put( "query.collectiontype" , entity.type );
        if( entity.type.fund?.objid ) {
            p.put("query.fundid", entity.type.fund?.objid);
        }
        p.onselect = { o->
            selectedItem.item = o;
            selectedItem.amount = o.remove("amount");
            selectedItem.remarks = o.remove("remarks");
        } 
        return Inv.lookupOpener("cashreceiptitem:lookup",p );
    }

    void updateBalances() {
        if(!entity.items) {
            entity.amount = 0;
        }
        else {
            entity.amount = NumberUtil.round( entity.items.sum{ it.amount } ); 
        }
        binding.refresh('entity.amount');        
    }
    
    public void afterColumnUpdate(String name, def item, String colName ) {
        if( colName == "amount" ) {
            updateBalances();
        }
    }
    
    public boolean isColumnEditable(String name, Object item, String columnName) {
        boolean b = super.isColumnEditable(name,item,columnName);
        if(!b) return false;
        if ( name == 'amount' ) { 
            def valuetype = item?.valuetype.toString().toUpperCase();
            if ( valuetype == 'FIXED' ) return false; 
        }
        return true;
    }
    
    public void addItem(String name, def item ) {
        item.objid = 'CRCI' + new java.rmi.server.UID();
        entity.items << item;
        updateBalances();
    }
    
    public boolean beforeRemoveItem(String name, def item ) {
        return MsgBox.confirm("You are about to remove this entry. Proceed?"); 
    }
    
    public void afterRemoveItem(String name, def item ) {
        updateBalances();
    } 
    
    public void post() {
        if(!MsgBox.confirm("You are about to post this document. Proceed?")) return;
        cmSvc.post([objid: entity.objid]);
        entity.state = 'POSTED';
    } 
    
}    