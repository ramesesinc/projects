package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;


public class WaterworksStubout {

    @Service( 'WaterworksStuboutService' )
    def svc;
    
    def title = "Stubout Entry";
    def entity;
    def itemstat;
    
    void init(){
        entity = [accounts:[]];
        title = "New Stubout Entry"
    }
    
    void open() { 
        entity = svc.open( entity ); 
    } 

    def save(){
        if(!MsgBox.confirm("You are about to create this record. Continue?")) return;
        
        svc.save( entity ); 
        return '_close';
    }

    def getLookupAccount(){
        return Inv.lookupOpener("waterworksaccount:lookup",[:]);
    }

    def selectedItem;
    def tableHandler = [
         fetchList: {
            return entity.accounts;
         },
         onColumnUpdate: {o,name-> 
             if ( name == 'account' && entity.accounts.find{ it.account.objid==o.account.objid }) 
                throw new Exception("Account already exists!");
         }, 
         onAddItem: { o->
            entity.accounts << o; 
         }
    ] as EditorListModel;

    void removeAccount(){
        if(!selectedItem) throw new Exception("No item selected!");
        entity.accounts.remove(selectedItem);
        tableHandler.reload();
    }

    void up(){
        def idx0 = itemstat.index; 
        if ( idx0 <= 0 ) return;
        
        def obj1 = entity.accounts[idx0];
        def obj2 = entity.accounts[idx0-1];
        entity.accounts[idx0-1]=obj1;
        entity.accounts[idx0]=obj2;
        tableHandler.refresh(); 
        tableHandler.setSelectedItem(idx0-1);
    }

    void down(){
        def idx0 = itemstat.index; 
        if ( idx0+1 >= entity.accounts.size() ) return; 
        
        def obj1 = entity.accounts[idx0];
        def obj2 = entity.accounts[idx0+1];
        entity.accounts[idx0+1]=obj1;        
        entity.accounts[idx0]=obj2;
        tableHandler.refresh(); 
        tableHandler.setSelectedItem(idx0+1);
    }
}