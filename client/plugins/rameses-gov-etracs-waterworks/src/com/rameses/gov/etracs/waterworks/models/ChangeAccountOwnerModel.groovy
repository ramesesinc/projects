package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class ChangeAccountOwnerModel {
    
    @Service('WaterworksAccountService') 
    def svc; 
    
    def entity = [:];
    def handler;
        
    void init() {
    }
    
    def doOk() { 
        if ( !entity.newowner?.objid ) throw new Exception('Please specify the new owner'); 
        if ( !entity.acctname ) throw new Exception('Please specify the account name'); 
        
        if ( handler ) handler( entity ); 
        
        return '_close';
    }
    
    def doCancel() { 
        return '_close'; 
    }
}