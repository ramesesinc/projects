package com.rameses.enterprise.treasury.models;


import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.UID;
import com.rameses.seti2.models.*;

public class CreditMemoModel extends CrudFormModel {

    def getLookupAccount() {
        return Inv.lookupOpener("creditmemoaccount:lookup", ["query.typeid": entity.type.objid] );            
    }

    def getLookupBankAccount() {
        def h = { o->
            entity.bankaccount = o;
            binding.refresh();
        }
        def fundid = entity.type.fund.objid;
        return Inv.lookupOpener( "bankaccount:lookup", [onselect: h, "query.fundid": fundid ]);
    }

    def submit(){
        if(!entity.items)    
            throw new Exception("Please include at least one item");
        if(entity.amount<=0)
            throw new Exception("Amount must not be zero");                
        entity = svc.create( entity );  
        MsgBox.alert('Record successfully saved'); 
        return "_close";
    }

    def listHandler = [
        createItem : { return [parentid:entity.objid] },
        validate   : { li -> 
            def item = li.item;
            if ( ! item.item )
                throw new Exception('Account is required.');
            if (item.amount == null || item.amount <= 0.0)
                throw new Exception('Amount is required.');
        },

        onAddItem  : { item ->
            item.objid = 'DCI' + new java.rmi.server.UID();
            entity.items << item;
        },

        onRemoveItem : { item ->
            if (MsgBox.confirm('Delete selected item?')){
                svc.removeItem(item);
                entity.items.remove(item);
                calcAmount();
                return true;
            }
            return false;
        },

        onCommitItem : {
            calcAmount();
        },

        fetchList : { 
            return entity.items;
        },

    ] as EditorListModel;

    void calcAmount(){
        entity.amount = entity.items.amount.sum();
        if (entity.amount == null)
            entity.amount = 0.0;
        binding.refresh('entity.amount');
    }
    
}    