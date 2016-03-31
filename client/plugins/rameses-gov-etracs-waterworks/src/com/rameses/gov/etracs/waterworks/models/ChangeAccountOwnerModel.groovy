package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class ChangeAccountOwnerModel {
    
    @Service('WaterworksAccountService') 
    def svc; 
    
    def account = [:];
    def entity = [:];
    
    @PropertyChangeListener 
    def changelisteners = [
        'entity.newowner': { o-> 
            println o;
        }
    ]; 
    
    void init() {
        def o = svc.initChangeOwner([ ownerid: account.owner.objid ]); 
        entity.currentowner = o; 
    }
}