package com.rameses.gov.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class UnremittedCollectionDetailMonitorModel extends DefaultListController { 

    @Service('UnremittedCollectionMonitorService') 
    def monitorSvc; 

    @Caller 
    def caller; 
    
    def collectorid; 
    
    protected void beforeFetchList(Map params) {
        params.collectorid = collectorid; 
    }
    
    public int getRows() {
        return 100;
    }

    def getTotalAmount() {
        try {
            println '> '+ caller.selectedEntity;
            return caller.selectedEntity.amount; 
        } catch(Throwable t) {
            return 0.0; 
        }
    }
} 