package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;


public class WaterworksStuboutEntry {
    
    def title;
    def entity;
    def accounts;
    def itemstat;

    void init(){
        entity = [:];
        title = "New Stubout Entry"
        accounts = [];
    }

    def save(){
        if(!MsgBox.confirm("You are about to create this record. Continue?")) return;
        return '_close';
    }

    def getLookupAccount(){
        return Inv.lookupOpener("waterworksaccount:lookup",[:]);
    }

    def selectedItem;
    def tableHandler = [
         fetchList: {
             return accounts;
         },
         onAddItem: { o->
            def exist = false;
            println o;
            accounts.each{ 
                if(o.objid == it.objid){
                    println o.objid + " = " + it.objid;
                    exist = true;
                    throw new Exception("Account already exists!");
                }
            }
            if(!exist) accounts << o;
         }
    ] as EditorListModel;

    void removeAccount(){
        if(!selectedItem) throw new Exception("No item selected!");
        accounts.remove(selectedItem);
        tableHandler.reload();
    }

    void up(){
        println itemstat.index;
    }

    void down(){
        println itemstat.index;
    }

    void doOrder(String m){
        def index = itemstat.index;
        if(m == 'up'){
            while(index > 0){
                
            }
        }
    }
    
}