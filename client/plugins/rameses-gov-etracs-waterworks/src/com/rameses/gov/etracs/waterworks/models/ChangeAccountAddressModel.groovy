package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class ChangeAccountAddressModel {
    
    @Service('WaterworksAccountService') 
    def svc; 
    
    def entity = [:];
    def handler;
        
    void init() {
    }
    
    def doOk() { 
        if ( handler ) handler( entity ); 
        
        return '_close';
    }
    
    def doCancel() { 
        return '_close'; 
    }
}