package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

public class StuboutSectionGeneralModel { 

    @Service('PersistenceService') 
    def persistenceSvc; 
    
    @Caller 
    def caller;

    def mode = 'read';
    def title = 'General Information'; 
    def entity = [:]; 
    
    void init() {
        entity.clear(); 
        entity.putAll( caller.entity ); 
    }
        
    void edit() {
        mode = 'edit'; 
    }
    
    void cancel() {
        mode = 'read'; 
        entity.clear(); 
        entity.putAll( caller.entity ); 
    }
    void save() {
        def m = [:]; 
        m.putAll( entity ); 
        m._schemaname = 'waterworks_stubout'; 
        def resp = persistenceSvc.update( m ); 
        
        mode = 'read';  

        if ( resp ) entity.putAll( resp ); 
    }
} 
