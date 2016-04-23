package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class StuboutModel extends CrudFormModel {
    
    def sections = Inv.lookupOpener( "waterworks_stubout:sections" );
    
    
    /*
    def itemstat;
    
    public boolean beforeColumnUpdate(String name, def item, String colName, def newValue) {
        if ( colName == 'account' && entity.accounts.find{ it.account.objid==newValue.objid }) 
                throw new Exception("Account already exists!");
        return true;        
    }
    
    public void afterAddItem(String name, def item ) {
        entity.accounts.eachWithIndex { o,idx->
            o.sortorder = idx;
        };
    }
    
    void up(){
        def idx0 = itemstat.index; 
        if ( idx0 <= 0 ) return;
        def obj1 = entity.accounts[idx0];
        def obj2 = entity.accounts[idx0-1];
        entity.accounts[idx0-1]=obj1;
        
        entity.accounts[idx0]=obj2;
        
        int s = obj1.sortorder;
        obj1.sortorder = obj2.sortorder;
        obj2.sortorder = s;
        
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
        
        int s = obj1.sortorder;
        obj1.sortorder = obj2.sortorder;
        obj2.sortorder = s;
        
        itemHandlers.accounts.refresh(); 
        itemHandlers.accounts.setSelectedItem(idx0+1);
    }
    */
    
    
}