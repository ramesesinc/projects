package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class WaterworksStubout extends CrudFormModel {
   
    public boolean beforeColumnUpdate(String name, def item, String colName, def newValue) {
        if ( colName == 'account' && entity.accounts.find{ it.account.objid==newValue.objid }) 
                throw new Exception("Account already exists!");
        return true;        
    }
    
    public void beforeSave(def mode){
        entity.accounts.eachWithIndex { o,idx->
            o.sortorder = idx;
        };
    }
    
    public void afterFetchItems( String name, def items ) {
        items.sort{ it.sortorder };
    }
    
    void up(){
        def idx0 = itemstat.index; 
        if ( idx0 <= 0 ) return;
        def obj1 = entity.accounts[idx0];
        def obj2 = entity.accounts[idx0-1];
        entity.accounts[idx0-1]=obj1;
        entity.accounts[idx0]=obj2;
        itemHandlers.accounts.refresh(); 
        itemHandlers.accounts.setSelectedItem(idx0-1);
    }

    void down(){
        def idx0 = itemstat.index; 
        if ( idx0+1 >= entity.accounts.size() ) return; 
        
        def obj1 = entity.accounts[idx0];
        def obj2 = entity.accounts[idx0+1];
        entity.accounts[idx0+1]=obj1;        
        entity.accounts[idx0]=obj2;
        itemHandlers.accounts.refresh(); 
        itemHandlers.accounts.setSelectedItem(idx0+1);
    }
    
    
    
}