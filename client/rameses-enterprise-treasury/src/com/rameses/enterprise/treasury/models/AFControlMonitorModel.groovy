package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.common.* 

class AFControlMonitorModel extends DefaultListController { 

    @Service('AFControlMonitorService') 
    def monitorSvc; 
    
    def collectorList = [];
    
    void init() {
        collectorList = monitorSvc.getCollectors(); 
    }
  
    @PropertyChangeListener 
    def changeListener = [
        'query.collector': { 
            reload(); 
        }
    ]; 
} 