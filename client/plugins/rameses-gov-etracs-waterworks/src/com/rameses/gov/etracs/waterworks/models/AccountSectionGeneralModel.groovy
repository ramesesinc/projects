package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class AccountSectionGeneralModel {
    
    @Service('WaterworksAccountService') 
    def acctSvc; 
    
    @Caller 
    def caller; 
    
    @Binding 
    def binding;
    
    def title = 'General Information';
    
    def getEntity() {
        return caller?.entity; 
    }
    
    def changeAddress(){ 
        def map = [ acctname: entity.acctname, address: entity.address ]; 
        map.newaddress = entity.address?.clone(); 
        if ( map.newaddress==null ) map.newaddress=[:];
        
        def params = [ entity: map ]; 
        params.handler = { o-> 
            def resp = acctSvc.postChangeAddress([ 
                            accountid: entity.objid, 
                            address: o.newaddress 
                        ]); 
            if ( resp?.address ) entity.address = resp.address; 
            
            binding.refresh();
        }
        return Inv.lookupOpener("waterworks_account:changeaddress", params );
    }
    
    def changeMeter(){
        return Inv.lookupOpener("waterworks_account:changemeter", [:]); 
    }
    
}