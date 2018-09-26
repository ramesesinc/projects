package com.rameses.enterprise.treasury.models;


import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.UID;
import com.rameses.seti2.models.*;
import com.rameses.util.*;


public class CreditMemoModel extends CrudFormModel {

    void afterCreate() {
        def collectiontype = null
        def filter = ["allowcreditmemo = 1 AND orgid IS NULL AND state='ACTIVE' "];
        def h = { o->
            collectiontype = o;
        }
        Modal.show("collectiontype:lookup", [customFilter:filter, onselect: h]);
        if(!collectiontype) throw new BreakException();
        entity.collectiontype = collectiontype;
        entity.items = [];
        entity.amount = 0;
    }
    
    def selectedItem;
    
    def getLookupItems() {
        def p = [:];
        p.put( "query.collectiontype" , entity.collectiontype );
        if( entity.collectiontype.fund?.objid ) {
            p.put("query.fundid", entity.collectiontype.fund?.objid);
        }
        p.onselect = { o->
            selectedItem.item = o;
            selectedItem.amount = o.remove("amount");
            selectedItem.remarks = o.remove("remarks");
        } 
        return Inv.lookupOpener("cashreceiptitem:lookup",p );
    }

    void updateBalances() {
        entity.amount = NumberUtil.round( entity.items.sum{ it.amount } ); 
        binding.refresh('entity.amount');        
    }
    
    def itemListModel = [
        fetchList: { o->
            return entity.items;
        },
        onAddItem: {o-> 
            o.objid = 'CRCI' + new java.rmi.server.UID();
            entity.items << o;
            entity.amount += o.amount;
            updateBalances();
        },
        isColumnEditable: { o,name-> 
            if ( name == 'amount' ) { 
                def valuetype = o?.item?.valuetype.toString().toUpperCase();
                if ( valuetype == 'FIXED' ) return false; 
            }
            return true; 
        }, 
        onColumnUpdate: {o,name-> 
            if(entity.items) {
                entity.amount = entity.items.sum{ it.amount };
                updateBalances();
            }
        },
        onRemoveItem: { o->
            if(!MsgBox.confirm("You are about to remove this entry. Proceed?")) 
                return false;
            entity.items.remove( o );
            if( entity.items ) {
                entity.amount = entity.items.sum{ it.amount };
            }
            else {
                entity.amount = 0;
            }
            updateBalances();
            return true;
        }
    ] as EditorListModel;

    /*
    def submit(){
        if(!entity.items)    
            throw new Exception("Please include at least one item");
        if(entity.amount<=0)
            throw new Exception("Amount must not be zero");                
        entity = svc.create( entity );  
        MsgBox.alert('Record successfully saved'); 
        return "_close";
    }

    */
    
}    