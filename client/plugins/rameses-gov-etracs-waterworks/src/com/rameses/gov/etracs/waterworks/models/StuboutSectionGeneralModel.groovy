package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

public class StuboutSectionGeneralModel { 

    @Caller 
    def caller;

    def mode = 'read';
    def title = 'General Information'; 
    
    def getEntity() {
        return caller?.entity; 
    } 
    
    void edit() {
        mode = 'edit'; 
    }
    
    void cancel() {
        
    }
} 
